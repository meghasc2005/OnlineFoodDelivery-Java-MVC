package com.fooddelivery.controller;

import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.User;
import com.fooddelivery.service.CartService;
import com.fooddelivery.service.FoodItemService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.service.impl.CartServiceImpl;
import com.fooddelivery.service.impl.FoodItemServiceImpl;
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
 * RestaurantMenuServlet — Displays the food menu for a specific restaurant.
 * URL: /menu
 */
public class RestaurantMenuServlet extends HttpServlet {

    private final RestaurantService restaurantService = new RestaurantServiceImpl();
    private final FoodItemService   foodItemService   = new FoodItemServiceImpl();
    private final CartService       cartService       = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);

        // Parse restaurantId safely
        int restaurantId = 0;
        try {
            String p = request.getParameter("restaurantId");
            if (p == null) p = request.getParameter("id");
            restaurantId = Integer.parseInt(p);
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/restaurants");
            return;
        }

        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);
        if (restaurant == null) {
            response.sendRedirect(request.getContextPath() + "/restaurants");
            return;
        }

        List<FoodItem> foods = foodItemService.getFoodsByRestaurant(restaurantId);
        int cartCount = cartService.getCartItemCount(user.getUserId());
        List<com.fooddelivery.model.Review> reviews = new com.fooddelivery.service.impl.ReviewServiceImpl().getReviewsByRestaurant(restaurantId);

        request.setAttribute("restaurant", restaurant);
        request.setAttribute("foods", foods);
        request.setAttribute("cartCount", cartCount);
        request.setAttribute("reviews", reviews);

        request.getRequestDispatcher("/WEB-INF/views/restaurantMenu.jsp")
               .forward(request, response);
    }
}
