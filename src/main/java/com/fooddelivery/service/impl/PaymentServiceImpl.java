package com.fooddelivery.service.impl;

import com.fooddelivery.dao.OrderDAO;
import com.fooddelivery.dao.PaymentDAO;
import com.fooddelivery.dao.impl.OrderDAOImpl;
import com.fooddelivery.dao.impl.PaymentDAOImpl;
import com.fooddelivery.model.Payment;
import com.fooddelivery.service.PaymentService;

import java.sql.SQLException;

/**
 * PaymentServiceImpl — Business logic implementation of {@link PaymentService}.
 *
 * <p>Processes payments by method (COD / UPI / CARD) with simulated validation.
 * On a successful payment, the corresponding order status is updated to
 * {@code "CONFIRMED"} via {@link OrderDAO}.
 * All {@link SQLException} instances are caught internally and logged.</p>
 */
public class PaymentServiceImpl implements PaymentService {

    // ===== DAO Dependencies =====
    private final PaymentDAO paymentDAO = new PaymentDAOImpl();
    private final OrderDAO   orderDAO   = new OrderDAOImpl();

    // ===== Service Method Implementations =====

    @Override
    public Payment processPayment(int orderId, int userId, double amount,
                                  String method, String transactionInput) {
        // 1. Build the Payment object
        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);

        // 2. Apply payment method logic
        switch (method != null ? method.toUpperCase() : "") {

            case "COD":
                // COD — always succeeds immediately
                payment.setStatus("SUCCESS");
                payment.setTransactionId("COD-" + orderId);
                break;

            case "UPI":
                // UPI — succeeds only if a UPI ID was provided
                if (transactionInput != null && !transactionInput.trim().isEmpty()) {
                    payment.setStatus("SUCCESS");
                    payment.setTransactionId("UPI-" + transactionInput.trim());
                } else {
                    payment.setStatus("FAILED");
                    payment.setTransactionId(null);
                }
                break;

            case "CARD":
                // CARD — dummy success (no real card validation in this demo)
                payment.setStatus("SUCCESS");
                payment.setTransactionId("CARD-TXN-" + System.currentTimeMillis());
                break;

            default:
                // Unknown method — mark as failed
                payment.setStatus("FAILED");
                payment.setTransactionId(null);
                break;
        }

        try {
            // 3. Persist the payment record
            paymentDAO.savePayment(payment);

            // 4. If payment succeeded, update order status to CONFIRMED
            if ("SUCCESS".equals(payment.getStatus())) {
                orderDAO.updateOrderStatus(orderId, "CONFIRMED");
            }

            return payment;

        } catch (SQLException e) {
            System.err.println("[PaymentServiceImpl] processPayment error: "
                    + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Payment getPaymentForOrder(int orderId) {
        if (orderId <= 0) {
            return null;
        }
        try {
            return paymentDAO.getPaymentByOrderId(orderId);
        } catch (SQLException e) {
            System.err.println("[PaymentServiceImpl] getPaymentForOrder error: "
                    + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
