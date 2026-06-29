package com.fooddelivery.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Payment — Entity representing the payment record associated with an order.
 *
 * <p>Maps to the {@code payments} table in {@code food_delivery_db}.
 * Every {@link Order} has exactly one corresponding {@code Payment} (enforced
 * by the {@code UNIQUE} constraint on {@code order_id}). The status transitions
 * from {@code PENDING} to {@code SUCCESS} or {@code FAILED} after processing.</p>
 */
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Fields =====
    private int       paymentId;
    private int       orderId;
    private int       userId;
    private double    amount;
    private String    paymentMethod;  // COD|UPI|CARD
    private String    transactionId;  // nullable for COD/PENDING
    private String    status;         // PENDING|SUCCESS|FAILED
    private Timestamp paidAt;

    // ===== No-Arg Constructor =====
    public Payment() {
    }

    // ===== All-Arg Constructor =====
    public Payment(int paymentId, int orderId, int userId, double amount,
                   String paymentMethod, String transactionId,
                   String status, Timestamp paidAt) {
        this.paymentId     = paymentId;
        this.orderId       = orderId;
        this.userId        = userId;
        this.amount        = amount;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.status        = status;
        this.paidAt        = paidAt;
    }

    // ===== Getters & Setters =====

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Timestamp paidAt) {
        this.paidAt = paidAt;
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "Payment{"
                + "paymentId=" + paymentId
                + ", orderId=" + orderId
                + ", userId=" + userId
                + ", amount=" + amount
                + ", paymentMethod='" + paymentMethod + '\''
                + ", transactionId='" + transactionId + '\''
                + ", status='" + status + '\''
                + ", paidAt=" + paidAt
                + '}';
    }
}
