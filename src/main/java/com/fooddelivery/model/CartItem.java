package com.fooddelivery.model;

import java.io.Serializable;

/**
 * CartItem — Entity representing a single line item within a customer's cart.
 *
 * <p>Maps to the {@code cart_items} table in {@code food_delivery_db}.
 * Each cart item links a {@link Cart} to a {@link FoodItem} with a quantity.
 * The {@code food} field is a transient convenience object populated by the
 * DAO join query — it is NOT persisted in the database.</p>
 */
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Persisted Fields =====
    private int      cartItemId;
    private int      cartId;
    private int      foodId;
    private int      quantity;

    // Transient - populated by CartDAO join query
    private FoodItem food;

    // ===== No-Arg Constructor =====
    public CartItem() {
    }

    // ===== All-Arg Constructor =====
    public CartItem(int cartItemId, int cartId, int foodId, int quantity, FoodItem food) {
        this.cartItemId = cartItemId;
        this.cartId     = cartId;
        this.foodId     = foodId;
        this.quantity   = quantity;
        this.food       = food;
    }

    // ===== Getters & Setters =====

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
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

    public FoodItem getFood() {
        return food;
    }

    public void setFood(FoodItem food) {
        this.food = food;
    }

    // ===== Convenience Method =====

    /**
     * Returns the line-item subtotal: {@code food.price × quantity}.
     * Returns {@code 0.0} if the transient {@code food} reference is null.
     *
     * @return subtotal as a {@code double}
     */
    public double getSubtotal() {
        return (food != null) ? food.getPrice() * quantity : 0.0;
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "CartItem{"
                + "cartItemId=" + cartItemId
                + ", cartId=" + cartId
                + ", foodId=" + foodId
                + ", quantity=" + quantity
                + ", food=" + food
                + '}';
    }
}
