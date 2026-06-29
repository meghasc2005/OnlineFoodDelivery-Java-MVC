package com.fooddelivery.controller;

import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.User;
import com.fooddelivery.service.CartService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.service.impl.CartServiceImpl;
import com.fooddelivery.service.impl.RestaurantServiceImpl;
import com.fooddelivery.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * RestaurantServlet — Displays all active restaurants for customers.
 * URL: /restaurants
 */
public class RestaurantServlet extends HttpServlet {

    private final RestaurantService restaurantService = new RestaurantServiceImpl();
    private final CartService       cartService       = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);

        List<Restaurant> restaurants = restaurantService.getAllActiveRestaurants();
        int cartCount = cartService.getCartItemCount(user.getUserId());

        request.setAttribute("restaurants", restaurants);
        request.setAttribute("cartCount", cartCount);

        request.getRequestDispatcher("/WEB-INF/views/restaurants.jsp")
               .forward(request, response);
    }
}
