package com.fooddelivery.controller;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.model.Payment;
import com.fooddelivery.model.User;
import com.fooddelivery.service.OrderService;
import com.fooddelivery.service.PaymentService;
import com.fooddelivery.service.impl.OrderServiceImpl;
import com.fooddelivery.service.impl.PaymentServiceImpl;
import com.fooddelivery.util.SessionUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

public class TrackOrderServlet extends HttpServlet {

    private final OrderService   orderService   = new OrderServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();

    private int getStatusRank(String st) {
        if (st == null) return 0;
        switch (st) {
            case "PLACED": return 0;
            case "CONFIRMED":
            case "ACCEPTED": return 1;
            case "PREPARING": return 2;
            case "FOOD_READY": return 3;
            case "ASSIGNED": return 4;
            case "OUT_FOR_DELIVERY": return 5;
            case "DELIVERED": return 6;
            default: return 99; // CANCELLED/REJECTED
        }
    }

    private void syncAutoProgression(Order o) {
        if (o == null || o.getCreatedAt() == null) return;
        String cur = o.getStatus();
        if ("DELIVERED".equals(cur) || "CANCELLED".equals(cur) || "REJECTED".equals(cur)) return;

        long elapsedSec = (System.currentTimeMillis() - o.getCreatedAt().getTime()) / 1000;
        String target = cur;
        if (elapsedSec > 130) target = "DELIVERED";
        else if (elapsedSec > 105) target = "OUT_FOR_DELIVERY";
        else if (elapsedSec > 80) target = "ASSIGNED";
        else if (elapsedSec > 55) target = "FOOD_READY";
        else if (elapsedSec > 30) target = "PREPARING";
        else if (elapsedSec > 10) target = "ACCEPTED";

        if (getStatusRank(target) > getStatusRank(cur)) {
            try {
                orderService.updateOrderStatus(o.getOrderId(), target);
                o.setStatus(target);
            } catch (Exception ignored) {}
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtil.requireLogin(request, response)) return;

        User user = SessionUtil.getLoggedUser(session);

        int orderId = 0;
        try {
            orderId = Integer.parseInt(request.getParameter("orderId"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        String action = request.getParameter("action");
        if ("status".equals(action)) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            Order o = orderService.getOrderById(orderId);
            if (o != null && o.getUserId() == user.getUserId()) {
                syncAutoProgression(o);
                long elapsedSec = o.getCreatedAt() != null ? (System.currentTimeMillis() - o.getCreatedAt().getTime()) / 1000 : 0;
                long remainingSecs = Math.max(0, 140 - elapsedSec);
                String notif = "Order progressing smoothly through our kitchen.";
                switch (o.getStatus()) {
                    case "PLACED": notif = "Order placed successfully."; break;
                    case "ACCEPTED":
                    case "CONFIRMED": notif = "Restaurant accepted your order!"; break;
                    case "PREPARING": notif = "Master chefs are preparing your meal!"; break;
                    case "FOOD_READY": notif = "Food is packed and ready for pickup!"; break;
                    case "ASSIGNED": notif = "Valet partner assigned to your delivery!"; break;
                    case "OUT_FOR_DELIVERY": notif = "Out for delivery! Valet arriving soon!"; break;
                    case "DELIVERED": remainingSecs = 0; notif = "Delivered! Bon Appétit!"; break;
                    case "REJECTED":
                    case "CANCELLED": remainingSecs = 0; notif = "Order was cancelled."; break;
                }
                String json = String.format("{\"status\":\"%s\",\"badgeClass\":\"%s\",\"remainingSecs\":%d,\"notification\":\"%s\"}",
                        o.getStatus(), o.getStatusBadgeClass(), remainingSecs, notif);
                response.getWriter().write(json);
            } else {
                response.getWriter().write("{\"error\":\"unauthorized\"}");
            }
            return;
        }

        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getUserId() != user.getUserId()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        syncAutoProgression(order);

        List<OrderItem> items   = orderService.getOrderItemsWithFood(orderId);
        Payment         payment = paymentService.getPaymentForOrder(orderId);

        long remainingSeconds = 140;
        if (order.getCreatedAt() != null) {
            long elapsedSec = (System.currentTimeMillis() - order.getCreatedAt().getTime()) / 1000;
            remainingSeconds = Math.max(0, 140 - elapsedSec);
        }
        if ("DELIVERED".equals(order.getStatus()) || "CANCELLED".equals(order.getStatus()) || "REJECTED".equals(order.getStatus())) {
            remainingSeconds = 0;
        }

        request.setAttribute("order", order);
        request.setAttribute("orderItems", items);
        request.setAttribute("payment", payment);
        request.setAttribute("remainingSeconds", remainingSeconds);

        request.getRequestDispatcher("/WEB-INF/views/trackOrder.jsp")
               .forward(request, response);
    }
}
