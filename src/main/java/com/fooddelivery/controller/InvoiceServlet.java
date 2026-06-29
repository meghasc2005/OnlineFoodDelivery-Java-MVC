package com.fooddelivery.controller;

import com.fooddelivery.dao.impl.UserDAOImpl;
import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.model.Payment;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.User;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.PaymentService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.service.impl.OrderServiceImpl;
import com.fooddelivery.service.impl.PaymentServiceImpl;
import com.fooddelivery.service.impl.RestaurantServiceImpl;
import com.fooddelivery.util.DBConnection;
import com.fooddelivery.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceServlet extends HttpServlet {

    private final OrderService      orderService      = new OrderServiceImpl();
    private final PaymentService    paymentService    = new PaymentServiceImpl();
    private final RestaurantService restaurantService = new RestaurantServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User loggedUser = SessionUtil.getLoggedUser(session);

        int orderId = 0;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
            if (orderId <= 0) throw new NumberFormatException("Invalid orderId");
        } catch (Exception e) {
            request.getRequestDispatcher("/WEB-INF/views/invoiceNotFound.jsp").forward(request, response);
            return;
        }

        // 1. Fetch Order
        Order order = orderService.getOrderById(orderId);
        if (order == null) {
            request.getRequestDispatcher("/WEB-INF/views/invoiceNotFound.jsp").forward(request, response);
            return;
        }

        // Authorization check
        if (order.getUserId() != loggedUser.getUserId() && !"ADMIN".equals(loggedUser.getRole())) {
            request.getRequestDispatcher("/WEB-INF/views/invoiceNotFound.jsp").forward(request, response);
            return;
        }

        // 2. Fetch Order Items
        List<OrderItem> items = orderService.getOrderItemsWithFood(orderId);
        if (items == null) items = new ArrayList<>();
        for (OrderItem item : items) {
            if (item.getFood() == null) {
                FoodItem dummyFood = new FoodItem();
                dummyFood.setName("Delicacy Dish Item #" + item.getFoodId());
                dummyFood.setVeg(true);
                item.setFood(dummyFood);
            }
        }

        // 3. Fetch Customer User
        User customerUser = null;
        try {
            customerUser = new UserDAOImpl().getUserById(order.getUserId());
        } catch (Exception ignored) {}
        if (customerUser == null) customerUser = loggedUser;

        // 4. Fetch Restaurant Details
        Restaurant restaurant = null;
        if (!items.isEmpty() && items.get(0).getFood() != null && items.get(0).getFood().getRestaurantId() > 0) {
            restaurant = restaurantService.getRestaurantById(items.get(0).getFood().getRestaurantId());
        }
        if (restaurant == null) {
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement("SELECT f.restaurant_id FROM order_items oi JOIN food_items f ON oi.food_id = f.food_id WHERE oi.order_id = ? LIMIT 1")) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        restaurant = restaurantService.getRestaurantById(rs.getInt(1));
                    }
                }
            } catch (Exception ignored) {}
        }

        // 5. Fetch Payment Details (with safe fallback if null)
        Payment payment = null;
        try {
            payment = paymentService.getPaymentForOrder(orderId);
        } catch (Exception ignored) {}
        if (payment == null) {
            payment = new Payment();
            payment.setPaymentMethod(order.getPaymentMethod() != null ? order.getPaymentMethod() : "COD");
            payment.setStatus("COMPLETED");
            payment.setAmount(order.getTotalAmount());
        }

        request.setAttribute("order", order);
        request.setAttribute("orderItems", items);
        request.setAttribute("payment", payment);
        request.setAttribute("restaurant", restaurant);
        request.setAttribute("user", customerUser);

        request.getRequestDispatcher("/WEB-INF/views/invoice.jsp").forward(request, response);
    }
}
