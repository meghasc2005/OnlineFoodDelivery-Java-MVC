package com.fooddelivery.service;

import com.fooddelivery.model.Review;
import java.util.List;

public interface ReviewService {
    boolean addReview(int orderId, int userId, int restaurantId, Integer foodId, int rating, String comment);
    List<Review> getReviewsByRestaurant(int restaurantId);
    boolean hasUserReviewedOrder(int orderId, int userId);
}
