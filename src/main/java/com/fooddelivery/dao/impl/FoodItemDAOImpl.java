package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.FoodItemDAO;
import com.fooddelivery.model.FoodItem;
import com.fooddelivery.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * FoodItemDAOImpl — JDBC implementation of {@link FoodItemDAO}.
 *
 * <p>All SQL is defined as private constants. Every database operation uses
 * try-with-resources. No business logic resides in this class.</p>
 */
public class FoodItemDAOImpl implements FoodItemDAO {

    // ===== SQL Constants =====
    private static final String FIND_BY_RESTAURANT =
            "SELECT * FROM food_items "
            + "WHERE restaurant_id = ? AND is_available = 1 "
            + "ORDER BY category, name";

    private static final String SEARCH =
            "SELECT * FROM food_items "
            + "WHERE (name LIKE ? OR category LIKE ?) AND is_available = 1";

    private static final String FIND_BY_ID =
            "SELECT * FROM food_items WHERE food_id = ?";

    private static final String FIND_ALL =
            "SELECT * FROM food_items ORDER BY restaurant_id, name";

    private static final String INSERT =
            "INSERT INTO food_items "
            + "(restaurant_id, name, description, price, category, image_url, is_available) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE food_items "
            + "SET restaurant_id = ?, name = ?, description = ?, price = ?, "
            + "    category = ?, image_url = ?, is_available = ? "
            + "WHERE food_id = ?";

    private static final String DELETE =
            "DELETE FROM food_items WHERE food_id = ?";

    // ===== Private Row Mapper =====

    /**
     * Maps a single {@link ResultSet} row to a {@link FoodItem} object.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a fully populated {@link FoodItem}
     * @throws SQLException if any column read fails
     */
    private FoodItem mapRow(ResultSet rs) throws SQLException {
        FoodItem item = new FoodItem();
        item.setFoodId(rs.getInt("food_id"));
        item.setRestaurantId(rs.getInt("restaurant_id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setPrice(rs.getDouble("price"));
        item.setCategory(rs.getString("category"));
        item.setImageUrl(rs.getString("image_url"));
        item.setAvailable(rs.getBoolean("is_available"));
        try { item.setVeg(rs.getBoolean("is_veg")); } catch (SQLException e) {}
        item.setCreatedAt(rs.getTimestamp("created_at"));
        return item;
    }

    // ===== DAO Method Implementations =====

    @Override
    public List<FoodItem> getFoodsByRestaurant(int restaurantId) throws SQLException {
        List<FoodItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_RESTAURANT)) {

            ps.setInt(1, restaurantId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<FoodItem> searchFoods(String keyword) throws SQLException {
        List<FoodItem> list = new ArrayList<>();
        String pattern = "%" + keyword + "%";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SEARCH)) {

            ps.setString(1, pattern);   // matches name LIKE %keyword%
            ps.setString(2, pattern);   // matches category LIKE %keyword%

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public FoodItem getFoodById(int foodId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {

            ps.setInt(1, foodId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<FoodItem> getAllFoods() throws SQLException {
        List<FoodItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public boolean addFood(FoodItem item) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            ps.setInt(1, item.getRestaurantId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getPrice());
            ps.setString(5, item.getCategory());
            ps.setString(6, item.getImageUrl());
            ps.setBoolean(7, item.isAvailable());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateFood(FoodItem item) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setInt(1, item.getRestaurantId());
            ps.setString(2, item.getName());
            ps.setString(3, item.getDescription());
            ps.setDouble(4, item.getPrice());
            ps.setString(5, item.getCategory());
            ps.setString(6, item.getImageUrl());
            ps.setBoolean(7, item.isAvailable());
            ps.setInt(8, item.getFoodId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteFood(int foodId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, foodId);
            return ps.executeUpdate() > 0;
        }
    }
}
