package com.fooddelivery.model;

import java.io.Serializable;

/**
 * OrderItem — Entity representing a single line item within a placed order.
 *
 * <p>Maps to the {@code order_items} table in {@code food_delivery_db}.
 * The {@code unitPrice} captures the food price AT THE TIME of ordering,
 * ensuring historical accuracy even if the food price changes later.
 * The {@code food} field is a transient reference populated by the service
 * layer for display purposes — it is NOT persisted in the database.</p>
 */
public class OrderItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Persisted Fields =====
    private int      orderItemId;
    private int      orderId;
    private int      foodId;
    private int      quantity;
    private double   unitPrice;

    // Transient - populated by service layer for display
    private FoodItem food;

    // ===== No-Arg Constructor =====
    public OrderItem() {
    }

    // ===== All-Arg Constructor =====
    public OrderItem(int orderItemId, int orderId, int foodId, int quantity,
                     double unitPrice, FoodItem food) {
        this.orderItemId = orderItemId;
        this.orderId     = orderId;
        this.foodId      = foodId;
        this.quantity    = quantity;
        this.unitPrice   = unitPrice;
        this.food        = food;
    }

    // ===== Getters & Setters =====

    public int getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        this.orderItemId = orderItemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public FoodItem getFood() {
        return food;
    }

    public void setFood(FoodItem food) {
        this.food = food;
    }

    // ===== Convenience Method =====

    /**
     * Returns the total cost for this line item: {@code unitPrice × quantity}.
     *
     * @return item total as a {@code double}
     */
    public double getItemTotal() {
        return unitPrice * quantity;
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "OrderItem{"
                + "orderItemId=" + orderItemId
                + ", orderId=" + orderId
                + ", foodId=" + foodId
                + ", quantity=" + quantity
                + ", unitPrice=" + unitPrice
                + ", food=" + food
                + '}';
    }
}
