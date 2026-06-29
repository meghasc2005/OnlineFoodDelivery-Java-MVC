package com.fooddelivery.controller;

import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.User;
import com.fooddelivery.service.CartService;
import com.fooddelivery.service.CouponService;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.impl.CartServiceImpl;
import com.fooddelivery.service.impl.CouponServiceImpl;
import com.fooddelivery.service.impl.OrderServiceImpl;
import com.fooddelivery.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartServiceImpl();
    private final OrderService orderService = new OrderServiceImpl();
    private final CouponService couponService = new CouponServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);
        List<CartItem> cartItems = cartService.getCartItems(user.getUserId());

        if (cartItems == null || cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        double subtotal = cartService.calculateCartTotal(cartItems);
        double gst = Math.round((subtotal * 0.05) * 100.0) / 100.0;
        double platformFee = (subtotal > 0) ? 5.0 : 0.0;
        double deliveryFee = (subtotal > 0 && subtotal < 149.0) ? 30.0 : 0.0;

        String appliedCoupon = (String) session.getAttribute("appliedCoupon");
        double discount = 0.0;
        if (appliedCoupon != null && subtotal > 0) {
            discount = couponService.calculateDiscount(appliedCoupon, subtotal, deliveryFee);
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
        request.setAttribute("userAddress", user.getAddress());

        request.getRequestDispatcher("/WEB-INF/views/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);

        String deliveryAddress = request.getParameter("deliveryAddress");
        String paymentMethod = request.getParameter("paymentMethod");
        String instructions = request.getParameter("instructions");

        deliveryAddress = (deliveryAddress != null) ? deliveryAddress.trim() : "";
        paymentMethod = (paymentMethod != null) ? paymentMethod.trim() : "COD";

        if (deliveryAddress.isEmpty()) {
            deliveryAddress = user.getAddress();
        }

        try {
            Order order = orderService.placeOrder(user.getUserId(), deliveryAddress, paymentMethod);

            if (order == null) {
                request.setAttribute("error", "Failed to place order. Please try again.");
                doGet(request, response);
                return;
            }

            session.removeAttribute("appliedCoupon");
            response.sendRedirect(request.getContextPath() + "/payment?orderId=" + order.getOrderId());

        } catch (RuntimeException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
