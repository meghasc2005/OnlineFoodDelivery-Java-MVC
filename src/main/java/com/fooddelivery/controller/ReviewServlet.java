package com.fooddelivery.controller;

import com.fooddelivery.model.User;
import com.fooddelivery.service.ReviewService;
import com.fooddelivery.service.impl.ReviewServiceImpl;
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

public class ReviewServlet extends HttpServlet {

    private final ReviewService reviewService = new ReviewServiceImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);

        try {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");

            int restaurantId = 0;
            try {
                restaurantId = Integer.parseInt(request.getParameter("restaurantId"));
            } catch (Exception ignored) {}

            if (restaurantId <= 0) {
                try (Connection c = DBConnection.getConnection();
                     PreparedStatement ps = c.prepareStatement("SELECT f.restaurant_id FROM order_items oi JOIN food_items f ON oi.food_id = f.food_id WHERE oi.order_id = ? LIMIT 1")) {
                    ps.setInt(1, orderId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) restaurantId = rs.getInt(1);
                    }
                }
            }

            Integer foodId = null;
            String fParam = request.getParameter("foodId");
            if (fParam != null && !fParam.trim().isEmpty()) {
                foodId = Integer.parseInt(fParam);
            }

            if (restaurantId > 0) {
                reviewService.addReview(orderId, user.getUserId(), restaurantId, foodId, rating, comment);
            }

            String ref = request.getHeader("Referer");
            if (ref != null) {
                response.sendRedirect(ref);
            } else {
                response.sendRedirect(request.getContextPath() + "/orders");
            }
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/orders");
        }
    }
}
