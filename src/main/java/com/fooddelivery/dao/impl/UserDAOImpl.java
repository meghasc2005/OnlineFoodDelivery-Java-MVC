package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.UserDAO;
import com.fooddelivery.model.User;
import com.fooddelivery.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * UserDAOImpl — JDBC implementation of {@link UserDAO}.
 *
 * <p>All SQL is defined as private constants. Every database operation uses
 * try-with-resources to guarantee connection and statement closure.
 * No business logic resides in this class.</p>
 */
public class UserDAOImpl implements UserDAO {

    // ===== SQL Constants =====
    private static final String FIND_BY_EMAIL =
            "SELECT * FROM users WHERE email = ?";

    private static final String FIND_BY_ID =
            "SELECT * FROM users WHERE user_id = ?";

    private static final String INSERT_USER =
            "INSERT INTO users (full_name, email, password, phone, address, role) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_USER =
            "UPDATE users SET full_name = ?, phone = ?, address = ? "
            + "WHERE user_id = ?";

    // ===== Private Row Mapper =====

    /**
     * Maps a single {@link ResultSet} row to a {@link User} object.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a fully populated {@link User}
     * @throws SQLException if any column read fails
     */
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setRole(rs.getString("role"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        return user;
    }

    // ===== DAO Method Implementations =====

    @Override
    public User getUserByEmail(String email) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_EMAIL)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public User getUserById(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean registerUser(User user) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getPhone());
            ps.setString(5, user.getAddress());
            ps.setString(6, user.getRole() != null ? user.getRole() : "CUSTOMER");

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getAddress());
            ps.setInt(4, user.getUserId());

            return ps.executeUpdate() > 0;
        }
    }
}
