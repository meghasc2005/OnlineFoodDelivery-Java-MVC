package com.fooddelivery.controller;

import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.User;
import com.fooddelivery.service.CartService;
import com.fooddelivery.service.FoodItemService;
import com.fooddelivery.service.impl.CartServiceImpl;
import com.fooddelivery.service.impl.FoodItemServiceImpl;
import com.fooddelivery.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

/**
 * SearchServlet — Handles keyword search for food items.
 * URL: /search
 */
public class SearchServlet extends HttpServlet {

    private final FoodItemService foodItemService = new FoodItemServiceImpl();
    private final CartService     cartService     = new CartServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);

        String keyword = request.getParameter("keyword");
        keyword = (keyword != null) ? keyword.trim() : "";

        List<FoodItem> searchResults = foodItemService.searchFoods(keyword);
        int cartCount = cartService.getCartItemCount(user.getUserId());

        request.setAttribute("searchResults", searchResults);
        request.setAttribute("keyword", keyword);
        request.setAttribute("cartCount", cartCount);

        request.getRequestDispatcher("/WEB-INF/views/restaurants.jsp")
               .forward(request, response);
    }
}
