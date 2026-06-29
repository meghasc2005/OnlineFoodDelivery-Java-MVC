package com.fooddelivery.dao;

import com.fooddelivery.model.Review;
import java.util.List;

public interface ReviewDAO {
    boolean addReview(Review review);
    List<Review> getReviewsByRestaurant(int restaurantId);
    double getAverageRatingByRestaurant(int restaurantId);
    boolean hasUserReviewedOrder(int orderId, int userId);
}
