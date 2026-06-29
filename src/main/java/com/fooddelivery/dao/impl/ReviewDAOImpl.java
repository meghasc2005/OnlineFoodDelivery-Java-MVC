package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.ReviewDAO;
import com.fooddelivery.model.Review;
import com.fooddelivery.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAOImpl implements ReviewDAO {

    @Override
    public boolean addReview(Review review) {
        String sql = "INSERT INTO reviews (order_id, user_id, restaurant_id, food_id, rating, comment) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, review.getOrderId());
            ps.setInt(2, review.getUserId());
            ps.setInt(3, review.getRestaurantId());
            if (review.getFoodId() != null) {
                ps.setInt(4, review.getFoodId());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setInt(5, review.getRating());
            ps.setString(6, review.getComment());
            int rows = ps.executeUpdate();
            
            // Auto update restaurant average rating
            updateRestaurantRating(conn, review.getRestaurantId());
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateRestaurantRating(Connection conn, int restaurantId) {
        String query = "SELECT AVG(rating) FROM reviews WHERE restaurant_id = ?";
        String update = "UPDATE restaurants SET rating = ? WHERE restaurant_id = ?";
        try (PreparedStatement ps1 = conn.prepareStatement(query)) {
            ps1.setInt(1, restaurantId);
            try (ResultSet rs = ps1.executeQuery()) {
                if (rs.next()) {
                    double avg = Math.round(rs.getDouble(1) * 10.0) / 10.0;
                    if (avg > 0) {
                        try (PreparedStatement ps2 = conn.prepareStatement(update)) {
                            ps2.setDouble(1, avg);
                            ps2.setInt(2, restaurantId);
                            ps2.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Review> getReviewsByRestaurant(int restaurantId) {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT r.*, u.full_name FROM reviews r JOIN users u ON r.user_id = u.user_id WHERE r.restaurant_id = ? ORDER BY r.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Review r = new Review();
                    r.setReviewId(rs.getInt("review_id"));
                    r.setOrderId(rs.getInt("order_id"));
                    r.setUserId(rs.getInt("user_id"));
                    r.setRestaurantId(rs.getInt("restaurant_id"));
                    int fId = rs.getInt("food_id");
                    if (!rs.wasNull()) r.setFoodId(fId);
                    r.setRating(rs.getInt("rating"));
                    r.setComment(rs.getString("comment"));
                    r.setCreatedAt(rs.getTimestamp("created_at"));
                    r.setUserName(rs.getString("full_name"));
                    list.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double getAverageRatingByRestaurant(int restaurantId) {
        String sql = "SELECT AVG(rating) FROM reviews WHERE restaurant_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, restaurantId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Math.round(rs.getDouble(1) * 10.0) / 10.0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public boolean hasUserReviewedOrder(int orderId, int userId) {
        String sql = "SELECT 1 FROM reviews WHERE order_id = ? AND user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
