package com.fooddelivery.model;

import java.sql.Timestamp;

public class Review {
    private int reviewId;
    private int orderId;
    private int userId;
    private int restaurantId;
    private Integer foodId;
    private int rating;
    private String comment;
    private Timestamp createdAt;
    private String userName; // For UI display

    public Review() {}

    public Review(int orderId, int userId, int restaurantId, Integer foodId, int rating, String comment) {
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.foodId = foodId;
        this.rating = rating;
        this.comment = comment;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRestaurantId() { return restaurantId; }
    public void setRestaurantId(int restaurantId) { this.restaurantId = restaurantId; }

    public Integer getFoodId() { return foodId; }
    public void setFoodId(Integer foodId) { this.foodId = foodId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
}
