package com.fooddelivery.controller;

import com.fooddelivery.service.FoodItemService;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.service.impl.FoodItemServiceImpl;
import com.fooddelivery.service.impl.OrderServiceImpl;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminDashboardServlet extends HttpServlet {

    private final RestaurantService restaurantService = new RestaurantServiceImpl();
    private final FoodItemService   foodItemService   = new FoodItemServiceImpl();
    private final OrderService      orderService      = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireAdmin(request, response)) return;

        long restaurantCount = restaurantService.getAllRestaurantsForAdmin().size();
        long foodCount       = foodItemService.getAllFoodsForAdmin().size();
        long orderCount      = orderService.getAllOrdersForAdmin().size();

        long todayOrders = 0;
        double todayRevenue = 0.0;
        long totalUsers = 0;
        long activeOutlets = 0;
        String topSellingDish = "Margherita Pizza";
        long placedCount = 0, preparingCount = 0, deliveredCount = 0, cancelledCount = 0;

        try (Connection conn = DBConnection.getConnection()) {
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*), COALESCE(SUM(total_amount), 0) FROM orders WHERE DATE(created_at) = CURDATE()")) {
                if (rs.next()) {
                    todayOrders = rs.getLong(1);
                    todayRevenue = rs.getDouble(2);
                }
            }
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM restaurants WHERE is_active = true")) {
                if (rs.next()) activeOutlets = rs.getLong(1);
            }
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'CUSTOMER'")) {
                if (rs.next()) totalUsers = rs.getLong(1);
            }
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT status, COUNT(*) FROM orders GROUP BY status")) {
                while (rs.next()) {
                    String stVal = rs.getString(1);
                    long c = rs.getLong(2);
                    if ("PLACED".equals(stVal) || "ACCEPTED".equals(stVal)) placedCount += c;
                    else if ("PREPARING".equals(stVal) || "FOOD_READY".equals(stVal) || "ASSIGNED".equals(stVal) || "OUT_FOR_DELIVERY".equals(stVal)) preparingCount += c;
                    else if ("DELIVERED".equals(stVal)) deliveredCount += c;
                    else if ("CANCELLED".equals(stVal) || "REJECTED".equals(stVal)) cancelledCount += c;
                }
            }
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT f.name FROM order_items oi JOIN food_items f ON oi.food_id = f.food_id GROUP BY f.food_id ORDER BY SUM(oi.quantity) DESC LIMIT 1")) {
                if (rs.next()) topSellingDish = rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("restaurantCount", restaurantCount);
        request.setAttribute("foodCount",       foodCount);
        request.setAttribute("orderCount",      orderCount);
        request.setAttribute("todayOrders",     todayOrders);
        request.setAttribute("todayRevenue",    todayRevenue);
        request.setAttribute("totalUsers",      totalUsers);
        request.setAttribute("activeOutlets",   activeOutlets);
        request.setAttribute("topSellingDish",  topSellingDish);
        request.setAttribute("placedCount",     placedCount);
        request.setAttribute("preparingCount",  preparingCount);
        request.setAttribute("deliveredCount",  deliveredCount);
        request.setAttribute("cancelledCount",  cancelledCount);

        request.getRequestDispatcher("/WEB-INF/views/admin/adminDashboard.jsp")
               .forward(request, response);
    }
}
