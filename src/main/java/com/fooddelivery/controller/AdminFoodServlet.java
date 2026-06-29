package com.fooddelivery.controller;

import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.service.FoodItemService;
import com.fooddelivery.service.RestaurantService;
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
 * AdminFoodServlet — Admin CRUD for food items.
 * URL: /admin/food
 */
public class AdminFoodServlet extends HttpServlet {

    private final FoodItemService   foodItemService   = new FoodItemServiceImpl();
    private final RestaurantService restaurantService = new RestaurantServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireAdmin(request, response)) return;

        List<FoodItem>   foods       = foodItemService.getAllFoodsForAdmin();
        List<Restaurant> restaurants = restaurantService.getAllRestaurantsForAdmin();

        request.setAttribute("foods",       foods);
        request.setAttribute("restaurants", restaurants);

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                FoodItem editFood = foodItemService.getFoodById(id);
                request.setAttribute("editFood", editFood);
            } catch (NumberFormatException e) {
                // Invalid ID — show list without edit form
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/manageFoods.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireAdmin(request, response)) return;

        String action = request.getParameter("action");
        action = (action != null) ? action.trim() : "";

        switch (action) {

            case "add": {
                FoodItem item = buildFoodItemFromParams(request);
                item.setAvailable(true);
                foodItemService.addFood(item);
                break;
            }

            case "edit": {
                FoodItem item = buildFoodItemFromParams(request);
                try {
                    item.setFoodId(Integer.parseInt(request.getParameter("foodId")));
                } catch (NumberFormatException ignored) {}
                String isAvailableParam = request.getParameter("isAvailable");
                item.setAvailable("on".equals(isAvailableParam) || "true".equals(isAvailableParam));
                foodItemService.updateFood(item);
                break;
            }

            case "delete": {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    foodItemService.deleteFood(id);
                } catch (NumberFormatException ignored) {}
                break;
            }

            default:
                break;
        }

        response.sendRedirect(request.getContextPath() + "/admin/food");
    }

    /** Builds a FoodItem model from request parameters (shared by add and edit). */
    private FoodItem buildFoodItemFromParams(HttpServletRequest request) {
        FoodItem item = new FoodItem();

        String name        = request.getParameter("name");
        String description = request.getParameter("description");
        String category    = request.getParameter("category");
        String imageUrl    = request.getParameter("imageUrl");
        String priceStr    = request.getParameter("price");
        String restIdStr   = request.getParameter("restaurantId");

        item.setName(       (name        != null) ? name.trim()        : "");
        item.setDescription((description != null) ? description.trim() : "");
        item.setCategory(   (category    != null) ? category.trim()    : "");
        item.setImageUrl(   (imageUrl    != null) ? imageUrl.trim()    : "");

        try {
            item.setPrice(Double.parseDouble(priceStr));
        } catch (NumberFormatException | NullPointerException e) {
            item.setPrice(0.0);
        }

        try {
            item.setRestaurantId(Integer.parseInt(restIdStr));
        } catch (NumberFormatException | NullPointerException e) {
            item.setRestaurantId(0);
        }

        return item;
    }
}
