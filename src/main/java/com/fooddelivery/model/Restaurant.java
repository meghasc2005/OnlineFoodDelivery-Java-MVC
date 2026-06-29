package com.fooddelivery.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Restaurant — Entity representing a food restaurant listed on FoodExpress.
 *
 * <p>Maps to the {@code restaurants} table in {@code food_delivery_db}.
 * Only restaurants where {@code isActive = true} are shown to customers.
 * Admins may toggle availability and manage restaurant details.</p>
 */
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Fields =====
    private int       restaurantId;
    private String    name;
    private String    description;
    private String    cuisineType;
    private String    address;
    private String    phone;
    private String    imageUrl;
    private double    rating;
    private String    deliveryTime;
    private double    deliveryFee;
    private String    openingHours = "10:00 AM - 11:00 PM";
    private double    minOrderAmount = 149.00;
    private String    offerBanner = "🔥 Flat 20% OFF above ₹299";
    private boolean   isActive;
    private Timestamp createdAt;

    // ===== No-Arg Constructor =====
    public Restaurant() {
    }

    // ===== All-Arg Constructor =====
    public Restaurant(int restaurantId, String name, String description,
                      String cuisineType, String address, String phone,
                      String imageUrl, double rating, String deliveryTime,
                      double deliveryFee, boolean isActive, Timestamp createdAt) {
        this.restaurantId = restaurantId;
        this.name         = name;
        this.description  = description;
        this.cuisineType  = cuisineType;
        this.address      = address;
        this.phone        = phone;
        this.imageUrl     = imageUrl;
        this.rating       = rating;
        this.deliveryTime = deliveryTime;
        this.deliveryFee  = deliveryFee;
        this.isActive     = isActive;
        this.createdAt    = createdAt;
    }

    // ===== Getters & Setters =====

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

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public String getOfferBanner() {
        return offerBanner;
    }

    public void setOfferBanner(String offerBanner) {
        this.offerBanner = offerBanner;
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "Restaurant{"
                + "restaurantId=" + restaurantId
                + ", name='" + name + '\''
                + ", cuisineType='" + cuisineType + '\''
                + ", address='" + address + '\''
                + ", phone='" + phone + '\''
                + ", rating=" + rating
                + ", deliveryTime='" + deliveryTime + '\''
                + ", deliveryFee=" + deliveryFee
                + ", isActive=" + isActive
                + ", createdAt=" + createdAt
                + '}';
    }
}
