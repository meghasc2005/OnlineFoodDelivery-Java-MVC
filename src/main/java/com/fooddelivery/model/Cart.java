package com.fooddelivery.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * Cart — Entity representing a customer's persistent shopping cart.
 *
 * <p>Maps to the {@code cart} table in {@code food_delivery_db}.
 * Each user may have at most ONE cart ({@code UNIQUE KEY} on {@code user_id}).
 * The {@code items} field is a transient convenience list of {@link CartItem}
 * objects populated by the service layer — it is NOT persisted in the database.</p>
 */
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Persisted Fields =====
    private int       cartId;
    private int       userId;
    private Integer   restaurantId;
    private Timestamp createdAt;

    // Transient - not persisted, loaded by service
    private List<CartItem> items;

    // ===== No-Arg Constructor =====
    public Cart() {
    }

    // ===== All-Arg Constructor =====
    public Cart(int cartId, int userId, Timestamp createdAt, List<CartItem> items) {
        this.cartId    = cartId;
        this.userId    = userId;
        this.createdAt = createdAt;
        this.items     = items;
    }

    // ===== Getters & Setters =====

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "Cart{"
                + "cartId=" + cartId
                + ", userId=" + userId
                + ", createdAt=" + createdAt
                + ", itemCount=" + (items != null ? items.size() : 0)
                + '}';
    }
}
