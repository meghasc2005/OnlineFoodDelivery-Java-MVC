package com.fooddelivery.controller;

import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.model.User;
import com.fooddelivery.service.CartService;
import com.fooddelivery.service.CouponService;
import com.fooddelivery.service.FoodItemService;
import com.fooddelivery.service.RestaurantService;
import com.fooddelivery.service.impl.CartServiceImpl;
import com.fooddelivery.service.impl.CouponServiceImpl;
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

public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();
    private final FoodItemService foodItemService = new FoodItemServiceImpl();
    private final RestaurantService restaurantService = new RestaurantServiceImpl();
    private final CouponService couponService = new CouponServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String action = request.getParameter("action");
        if (action != null) action = action.trim();

        // AJAX count request handling
        if ("count".equalsIgnoreCase(action)) {
            response.setContentType("application/json");
            User u = (session != null) ? SessionUtil.getLoggedUser(session) : null;
            int count = 0;
            if (u != null) {
                List<CartItem> items = cartService.getCartItems(u.getUserId());
                count = items.size();
            }
            response.getWriter().write("{\"count\": " + count + "}");
            return;
        }

        if (!SessionUtil.requireLogin(request, response)) return;
        User user = SessionUtil.getLoggedUser(session);

        List<CartItem> cartItems = cartService.getCartItems(user.getUserId());
        double subtotal = cartService.calculateCartTotal(cartItems);

        double gst = Math.round((subtotal * 0.05) * 100.0) / 100.0;
        double platformFee = (subtotal > 0) ? 5.0 : 0.0;
        double deliveryFee = (subtotal > 0 && subtotal < 149.0) ? 30.0 : 0.0;

        String appliedCoupon = (String) session.getAttribute("appliedCoupon");
        double discount = 0.0;
        if (appliedCoupon != null && subtotal > 0) {
            discount = couponService.calculateDiscount(appliedCoupon, subtotal, deliveryFee);
        } else if (subtotal == 0) {
            session.removeAttribute("appliedCoupon");
            appliedCoupon = null;
        }

        double grandTotal = Math.max(0.0, Math.round((subtotal + gst + platformFee + deliveryFee - discount) * 100.0) / 100.0);

        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartSubtotal", subtotal);
        request.setAttribute("cartGst", gst);
        request.setAttribute("cartPlatformFee", platformFee);
        request.setAttribute("cartDeliveryFee", deliveryFee);
        request.setAttribute("cartDiscount", discount);
        request.setAttribute("appliedCoupon", appliedCoupon);
        request.setAttribute("cartTotal", grandTotal);
        request.setAttribute("cartCount", cartItems.size());
        request.setAttribute("availableCoupons", couponService.getAllActiveCoupons());

        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);
        String action = request.getParameter("action");
        action = (action != null) ? action.trim() : "";

        try {
            switch (action) {

                case "add": {
                    int foodId = Integer.parseInt(request.getParameter("foodId"));
                    String qtyParam = request.getParameter("quantity");
                    int qty = (qtyParam != null && !qtyParam.trim().isEmpty())
                              ? Integer.parseInt(qtyParam.trim()) : 1;

                    List<CartItem> existingItems = cartService.getCartItems(user.getUserId());
                    FoodItem targetFood = foodItemService.getFoodById(foodId);

                    if (!existingItems.isEmpty() && targetFood != null) {
                        int currentRestId = existingItems.get(0).getFood().getRestaurantId();
                        if (currentRestId != targetFood.getRestaurantId()) {
                            session.setAttribute("diffRestaurantModal", true);
                            session.setAttribute("pendingFoodId", foodId);
                            session.setAttribute("pendingQty", qty);
                            Restaurant r = restaurantService.getRestaurantById(currentRestId);
                            session.setAttribute("currentRestName", r != null ? r.getName() : "another restaurant");
                            
                            String ref = request.getHeader("Referer");
                            if (ref != null && !ref.contains("/cart")) {
                                response.sendRedirect(ref);
                            } else {
                                response.sendRedirect(request.getContextPath() + "/menu?restaurantId=" + targetFood.getRestaurantId());
                            }
                            return;
                        }
                    }

                    cartService.addToCart(user.getUserId(), foodId, qty);
                    String ref = request.getHeader("Referer");
                    if (ref != null && ref.contains("/menu")) {
                        response.sendRedirect(ref);
                        return;
                    }
                    break;
                }

                case "clearAndAdd": {
                    String fIdParam = request.getParameter("foodId");
                    String qParam = request.getParameter("quantity");
                    int foodId = (fIdParam != null) ? Integer.parseInt(fIdParam) : (Integer) session.getAttribute("pendingFoodId");
                    int qty = (qParam != null) ? Integer.parseInt(qParam) : (Integer) session.getAttribute("pendingQty");

                    cartService.clearCart(user.getUserId());
                    cartService.addToCart(user.getUserId(), foodId, qty);

                    session.removeAttribute("diffRestaurantModal");
                    session.removeAttribute("pendingFoodId");
                    session.removeAttribute("pendingQty");
                    session.removeAttribute("currentRestName");

                    String ref = request.getHeader("Referer");
                    if (ref != null) {
                        response.sendRedirect(ref);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/cart");
                    }
                    return;
                }

                case "update": {
                    int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                    int qty = Integer.parseInt(request.getParameter("quantity").trim());
                    cartService.updateQuantity(cartItemId, qty);
                    break;
                }

                case "remove": {
                    int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
                    cartService.removeItem(cartItemId);
                    break;
                }

                case "applyCoupon": {
                    String code = request.getParameter("code");
                    if (code != null && !code.trim().isEmpty()) {
                        session.setAttribute("appliedCoupon", code.trim().toUpperCase());
                    }
                    break;
                }

                case "removeCoupon": {
                    session.removeAttribute("appliedCoupon");
                    break;
                }

                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }
}
