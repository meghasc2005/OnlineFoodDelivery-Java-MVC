package com.fooddelivery.controller;

import com.fooddelivery.model.Order;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.impl.OrderServiceImpl;
import com.fooddelivery.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * AdminOrderServlet — Admin view and status management for all orders.
 * URL: /admin/orders
 */
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderServiceImpl();

    /** All valid order status values available in the status-update dropdown. */
    private static final String[] STATUS_OPTIONS = {
        "PLACED", "ACCEPTED", "PREPARING", "FOOD_READY", "ASSIGNED", "OUT_FOR_DELIVERY", "DELIVERED", "REJECTED", "CANCELLED"
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireAdmin(request, response)) return;

        List<Order> orders = orderService.getAllOrdersForAdmin();
        request.setAttribute("orders", orders);
        request.setAttribute("statusOptions", STATUS_OPTIONS);

        request.getRequestDispatcher("/WEB-INF/views/admin/viewOrders.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireAdmin(request, response)) return;

        String action = request.getParameter("action");
        action = (action != null) ? action.trim() : "";

        if ("updateStatus".equals(action)) {
            try {
                int    orderId = Integer.parseInt(request.getParameter("orderId"));
                String status  = request.getParameter("status");
                if (status != null && !status.trim().isEmpty()) {
                    orderService.updateOrderStatus(orderId, status.trim());
                }
            } catch (NumberFormatException ignored) {
                // Invalid orderId — redirect without update
            }
        }

        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}
