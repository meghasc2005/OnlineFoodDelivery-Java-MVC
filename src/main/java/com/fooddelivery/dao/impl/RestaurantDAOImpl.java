package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.RestaurantDAO;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RestaurantDAOImpl — JDBC implementation of {@link RestaurantDAO}.
 *
 * <p>All SQL is defined as private constants. Every database operation uses
 * try-with-resources. No business logic resides in this class.</p>
 */
public class RestaurantDAOImpl implements RestaurantDAO {

    // ===== SQL Constants =====
    private static final String FIND_ALL_ACTIVE =
            "SELECT * FROM restaurants WHERE is_active = 1 ORDER BY name ASC";

    private static final String FIND_ALL =
            "SELECT * FROM restaurants ORDER BY name ASC";

    private static final String FIND_BY_ID =
            "SELECT * FROM restaurants WHERE restaurant_id = ?";

    private static final String INSERT =
            "INSERT INTO restaurants "
            + "(name, description, cuisine_type, address, phone, image_url, rating, delivery_time, delivery_fee, is_active) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE restaurants "
            + "SET name = ?, description = ?, cuisine_type = ?, address = ?, "
            + "    phone = ?, image_url = ?, rating = ?, delivery_time = ?, delivery_fee = ?, is_active = ? "
            + "WHERE restaurant_id = ?";

    private static final String DELETE =
            "DELETE FROM restaurants WHERE restaurant_id = ?";

    // ===== Private Row Mapper =====

    /**
     * Maps a single {@link ResultSet} row to a {@link Restaurant} object.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a fully populated {@link Restaurant}
     * @throws SQLException if any column read fails
     */
    private Restaurant mapRow(ResultSet rs) throws SQLException {
        Restaurant r = new Restaurant();
        r.setRestaurantId(rs.getInt("restaurant_id"));
        r.setName(rs.getString("name"));
        r.setDescription(rs.getString("description"));
        r.setCuisineType(rs.getString("cuisine_type"));
        r.setAddress(rs.getString("address"));
        r.setPhone(rs.getString("phone"));
        r.setImageUrl(rs.getString("image_url"));
        r.setRating(rs.getDouble("rating"));
        r.setDeliveryTime(rs.getString("delivery_time"));
        r.setDeliveryFee(rs.getDouble("delivery_fee"));
        r.setActive(rs.getBoolean("is_active"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }

    // ===== DAO Method Implementations =====

    @Override
    public List<Restaurant> getAllActiveRestaurants() throws SQLException {
        List<Restaurant> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_ACTIVE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<Restaurant> getAllRestaurants() throws SQLException {
        List<Restaurant> list = new ArrayList<>();
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
    public Restaurant getRestaurantById(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean addRestaurant(Restaurant r) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT)) {

            ps.setString(1, r.getName());
            ps.setString(2, r.getDescription());
            ps.setString(3, r.getCuisineType());
            ps.setString(4, r.getAddress());
            ps.setString(5, r.getPhone());
            ps.setString(6, r.getImageUrl());
            ps.setDouble(7, r.getRating());
            ps.setString(8, r.getDeliveryTime());
            ps.setDouble(9, r.getDeliveryFee());
            ps.setBoolean(10, r.isActive());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateRestaurant(Restaurant r) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE)) {

            ps.setString(1, r.getName());
            ps.setString(2, r.getDescription());
            ps.setString(3, r.getCuisineType());
            ps.setString(4, r.getAddress());
            ps.setString(5, r.getPhone());
            ps.setString(6, r.getImageUrl());
            ps.setDouble(7, r.getRating());
            ps.setString(8, r.getDeliveryTime());
            ps.setDouble(9, r.getDeliveryFee());
            ps.setBoolean(10, r.isActive());
            ps.setInt(11, r.getRestaurantId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean deleteRestaurant(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
