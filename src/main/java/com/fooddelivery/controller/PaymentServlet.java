package com.fooddelivery.controller;

import com.fooddelivery.model.Order;
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

/**
 * PaymentServlet — Displays payment form, processes payment, and shows result.
 * URL: /payment
 *
 * GET  /payment?orderId=X           → payment form
 * GET  /payment?orderId=X&result=success → success page
 * GET  /payment?orderId=X&result=failure → failure page
 * POST → processes payment → PRG redirect with result param
 */
public class PaymentServlet extends HttpServlet {

    private final OrderService   orderService   = new OrderServiceImpl();
    private final PaymentService paymentService = new PaymentServiceImpl();

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

        Order order = orderService.getOrderById(orderId);

        // Verify order exists and belongs to this user
        if (order == null || order.getUserId() != user.getUserId()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        // Handle result param for success/failure display
        String result = request.getParameter("result");
        if ("success".equals(result)) {
            Payment payment = paymentService.getPaymentForOrder(orderId);
            request.setAttribute("order", order);
            request.setAttribute("payment", payment);
            request.getRequestDispatcher("/WEB-INF/views/paymentSuccess.jsp")
                   .forward(request, response);
            return;
        }
        if ("failure".equals(result)) {
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/views/paymentFailure.jsp")
                   .forward(request, response);
            return;
        }

        // Default: show payment form
        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/views/payment.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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

        Order order = orderService.getOrderById(orderId);
        if (order == null || order.getUserId() != user.getUserId()) {
            response.sendRedirect(request.getContextPath() + "/orders");
            return;
        }

        String method     = request.getParameter("paymentMethod");
        String txnInput   = request.getParameter("transactionInput");
        method   = (method   != null) ? method.trim() : "";
        txnInput = (txnInput != null) ? txnInput.trim() : null;

        Payment payment = paymentService.processPayment(
                orderId, user.getUserId(), order.getTotalAmount(), method, txnInput);

        if (payment != null && "SUCCESS".equals(payment.getStatus())) {
            response.sendRedirect(
                    request.getContextPath() + "/payment?orderId=" + orderId + "&result=success");
        } else {
            response.sendRedirect(
                    request.getContextPath() + "/payment?orderId=" + orderId + "&result=failure");
        }
    }
}
