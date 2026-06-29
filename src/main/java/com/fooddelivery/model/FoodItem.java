package com.fooddelivery.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * FoodItem — Entity representing a single dish or beverage offered by a restaurant.
 *
 * <p>Maps to the {@code food_items} table in {@code food_delivery_db}.
 * Each {@code FoodItem} belongs to exactly one {@link Restaurant} via
 * {@code restaurantId}. Items with {@code isAvailable = false} are hidden
 * from the customer menu but retained in order history.</p>
 */
public class FoodItem implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Fields =====
    private int       foodId;
    private int       restaurantId;
    private String    name;
    private String    description;
    private double    price;
    private String    category;
    private String    imageUrl;
    private boolean   isAvailable;
    private boolean   isVeg = true;
    private Timestamp createdAt;

    // ===== No-Arg Constructor =====
    public FoodItem() {
    }

    // ===== All-Arg Constructor =====
    public FoodItem(int foodId, int restaurantId, String name, String description,
                    double price, String category, String imageUrl,
                    boolean isAvailable, Timestamp createdAt) {
        this.foodId       = foodId;
        this.restaurantId = restaurantId;
        this.name         = name;
        this.description  = description;
        this.price        = price;
        this.category     = category;
        this.imageUrl     = imageUrl;
        this.isAvailable  = isAvailable;
        this.createdAt    = createdAt;
    }

    // ===== Getters & Setters =====

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isVeg() {
        return isVeg;
    }

    public void setVeg(boolean isVeg) {
        this.isVeg = isVeg;
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "FoodItem{"
                + "foodId=" + foodId
                + ", restaurantId=" + restaurantId
                + ", name='" + name + '\''
                + ", price=" + price
                + ", category='" + category + '\''
                + ", isAvailable=" + isAvailable
                + ", createdAt=" + createdAt
                + '}';
    }
}
