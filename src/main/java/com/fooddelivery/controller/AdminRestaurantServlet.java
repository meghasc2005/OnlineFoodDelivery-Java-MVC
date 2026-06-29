package com.fooddelivery.controller;

import com.fooddelivery.model.Restaurant;
import com.fooddelivery.service.RestaurantService;
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
 * AdminRestaurantServlet — Admin CRUD for restaurants.
 * URL: /admin/restaurant
 */
public class AdminRestaurantServlet extends HttpServlet {

    private final RestaurantService restaurantService = new RestaurantServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireAdmin(request, response)) return;

        List<Restaurant> restaurants = restaurantService.getAllRestaurantsForAdmin();
        request.setAttribute("restaurants", restaurants);

        String action = request.getParameter("action");
        if ("edit".equals(action)) {
            try {
                int id = Integer.parseInt(request.getParameter("id"));
                Restaurant r = restaurantService.getRestaurantById(id);
                request.setAttribute("editRestaurant", r);
            } catch (NumberFormatException e) {
                // Invalid ID — just show list without edit form
            }
        }

        request.getRequestDispatcher("/WEB-INF/views/admin/manageRestaurants.jsp")
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
                Restaurant r = buildRestaurantFromParams(request);
                r.setActive(true);
                restaurantService.addRestaurant(r);
                break;
            }

            case "edit": {
                Restaurant r = buildRestaurantFromParams(request);
                try {
                    r.setRestaurantId(Integer.parseInt(
                            request.getParameter("restaurantId")));
                } catch (NumberFormatException ignored) {}
                String isActiveParam = request.getParameter("isActive");
                r.setActive("on".equals(isActiveParam) || "true".equals(isActiveParam));
                restaurantService.updateRestaurant(r);
                break;
            }

            case "delete": {
                try {
                    int id = Integer.parseInt(request.getParameter("id"));
                    restaurantService.deleteRestaurant(id);
                } catch (NumberFormatException ignored) {}
                break;
            }

            default:
                break;
        }

        response.sendRedirect(request.getContextPath() + "/admin/restaurant");
    }

    /** Builds a Restaurant model from request parameters (shared by add and edit). */
    private Restaurant buildRestaurantFromParams(HttpServletRequest request) {
        Restaurant r = new Restaurant();

        String name        = request.getParameter("name");
        String description = request.getParameter("description");
        String cuisineType = request.getParameter("cuisineType");
        String address     = request.getParameter("address");
        String phone       = request.getParameter("phone");
        String imageUrl    = request.getParameter("imageUrl");
        String ratingStr   = request.getParameter("rating");

        r.setName(       (name        != null) ? name.trim()        : "");
        r.setDescription((description != null) ? description.trim() : "");
        r.setCuisineType((cuisineType != null) ? cuisineType.trim() : "");
        r.setAddress(    (address     != null) ? address.trim()     : "");
        r.setPhone(      (phone       != null) ? phone.trim()       : "");
        r.setImageUrl(   (imageUrl    != null) ? imageUrl.trim()    : "");

        try {
            r.setRating(Double.parseDouble(ratingStr));
        } catch (NumberFormatException | NullPointerException e) {
            r.setRating(0.0);
        }

        return r;
    }
}
