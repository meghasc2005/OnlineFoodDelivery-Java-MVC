package com.fooddelivery.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;

    private int couponId;
    private String code;
    private String description;
    private String discountType; // PERCENT, FLAT, FREE_DELIVERY
    private double discountValue;
    private double minOrderAmount;
    private double maxDiscount;
    private boolean isActive;
    private Timestamp createdAt;

    public Coupon() {}

    public Coupon(int couponId, String code, String description, String discountType, double discountValue, double minOrderAmount, double maxDiscount, boolean isActive, Timestamp createdAt) {
        this.couponId = couponId;
        this.code = code;
        this.description = description;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderAmount = minOrderAmount;
        this.maxDiscount = maxDiscount;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public int getCouponId() { return couponId; }
    public void setCouponId(int couponId) { this.couponId = couponId; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public double getDiscountValue() { return discountValue; }
    public void setDiscountValue(double discountValue) { this.discountValue = discountValue; }

    public double getMinOrderAmount() { return minOrderAmount; }
    public void setMinOrderAmount(double minOrderAmount) { this.minOrderAmount = minOrderAmount; }

    public double getMaxDiscount() { return maxDiscount; }
    public void setMaxDiscount(double maxDiscount) { this.maxDiscount = maxDiscount; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public double calculateDiscount(double subtotal, double deliveryFee) {
        if (!isActive || subtotal < minOrderAmount) return 0.0;
        if ("FREE_DELIVERY".equalsIgnoreCase(discountType)) {
            return deliveryFee;
        } else if ("FLAT".equalsIgnoreCase(discountType)) {
            return discountValue;
        } else if ("PERCENT".equalsIgnoreCase(discountType)) {
            double calc = (subtotal * discountValue) / 100.0;
            return (maxDiscount > 0 && calc > maxDiscount) ? maxDiscount : calc;
        }
        return 0.0;
    }
}
