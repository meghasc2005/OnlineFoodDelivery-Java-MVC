package com.fooddelivery.service.impl;

import com.fooddelivery.dao.ReviewDAO;
import com.fooddelivery.dao.impl.ReviewDAOImpl;
import com.fooddelivery.model.Review;
import com.fooddelivery.service.ReviewService;

import java.util.List;

public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO = new ReviewDAOImpl();

    @Override
    public boolean addReview(int orderId, int userId, int restaurantId, Integer foodId, int rating, String comment) {
        if (rating < 1 || rating > 5) return false;
        if (reviewDAO.hasUserReviewedOrder(orderId, userId)) return false;
        Review rev = new Review(orderId, userId, restaurantId, foodId, rating, comment);
        return reviewDAO.addReview(rev);
    }

    @Override
    public List<Review> getReviewsByRestaurant(int restaurantId) {
        return reviewDAO.getReviewsByRestaurant(restaurantId);
    }

    @Override
    public boolean hasUserReviewedOrder(int orderId, int userId) {
        return reviewDAO.hasUserReviewedOrder(orderId, userId);
    }
}
