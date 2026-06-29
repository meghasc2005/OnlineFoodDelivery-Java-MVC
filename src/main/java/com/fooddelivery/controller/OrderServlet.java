package com.fooddelivery.controller;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.User;
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
 * OrderServlet — Displays order history for the logged-in customer.
 * URL: /orders
 */
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);

        List<Order> orders = orderService.getUserOrders(user.getUserId());
        request.setAttribute("orders", orders);

        request.getRequestDispatcher("/WEB-INF/views/orders.jsp")
               .forward(request, response);
    }
}
