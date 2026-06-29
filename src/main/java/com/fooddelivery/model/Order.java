package com.fooddelivery.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Order — Entity representing a customer's food order on FoodExpress.
 *
 * <p>Maps to the {@code orders} table in {@code food_delivery_db}.
 * An order progresses through statuses: {@code PLACED → CONFIRMED →
 * PREPARING → OUT_FOR_DELIVERY → DELIVERED} (or {@code CANCELLED}).
 * Each order is associated with a {@link Payment} record.</p>
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Fields =====
    private int       orderId;
    private int       userId;
    private double    totalAmount;
    private String    deliveryAddress;
    private String    status;         // PLACED|CONFIRMED|PREPARING|OUT_FOR_DELIVERY|DELIVERED|CANCELLED
    private String    paymentMethod;  // COD|UPI|CARD
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ===== No-Arg Constructor =====
    public Order() {
    }

    // ===== All-Arg Constructor =====
    public Order(int orderId, int userId, double totalAmount,
                 String deliveryAddress, String status, String paymentMethod,
                 Timestamp createdAt, Timestamp updatedAt) {
        this.orderId         = orderId;
        this.userId          = userId;
        this.totalAmount     = totalAmount;
        this.deliveryAddress = deliveryAddress;
        this.status          = status;
        this.paymentMethod   = paymentMethod;
        this.createdAt       = createdAt;
        this.updatedAt       = updatedAt;
    }

    // ===== Getters & Setters =====

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

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ===== Fee Breakdown Getters for Invoice View =====
    public double getGstAmount() {
        return Math.round((totalAmount * 0.05) * 100.0) / 100.0;
    }

    public double getPlatformFee() {
        return totalAmount > 0 ? 5.00 : 0.00;
    }

    public double getDeliveryFee() {
        return (totalAmount > 0 && totalAmount < 149.0) ? 30.00 : 0.00;
    }

    public double getDiscountAmount() {
        return 0.00;
    }

    public String getCouponCode() {
        return "FREE_PLATFORM";
    }

    // ===== Helper Methods =====

    /**
     * Returns the Bootstrap 5 badge CSS class corresponding to the current
     * order status. Used directly in JSP views: {@code class="badge ${order.statusBadgeClass}"}.
     *
     * @return Bootstrap badge class string
     */
    public String getStatusBadgeClass() {
        if (status == null) {
            return "bg-secondary";
        }
        switch (status) {
            case "PLACED":            return "bg-secondary";
            case "ACCEPTED":
            case "CONFIRMED":         return "bg-info text-dark";
            case "PREPARING":         return "bg-warning text-dark";
            case "FOOD_READY":        return "bg-primary-subtle text-primary border border-primary";
            case "ASSIGNED":          return "bg-info-subtle text-dark border border-info";
            case "OUT_FOR_DELIVERY":  return "bg-primary";
            case "DELIVERED":         return "bg-success";
            case "REJECTED":
            case "CANCELLED":         return "bg-danger";
            default:                  return "bg-secondary";
        }
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "Order{"
                + "orderId=" + orderId
                + ", userId=" + userId
                + ", totalAmount=" + totalAmount
                + ", deliveryAddress='" + deliveryAddress + '\''
                + ", status='" + status + '\''
                + ", paymentMethod='" + paymentMethod + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
