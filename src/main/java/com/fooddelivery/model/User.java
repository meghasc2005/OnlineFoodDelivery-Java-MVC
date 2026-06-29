package com.fooddelivery.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * User — Entity representing a registered user of the FoodExpress platform.
 *
 * <p>Maps to the {@code users} table in {@code food_delivery_db}.
 * A user may hold the role {@code "CUSTOMER"} (default) or {@code "ADMIN"}.
 * Passwords are stored as BCrypt hashes and must NEVER be stored in plain text.</p>
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    // ===== Fields =====
    private int       userId;
    private String    fullName;
    private String    email;
    private String    password;   // BCrypt hash — never plain text
    private String    phone;
    private String    address;
    private String    role;       // "CUSTOMER" or "ADMIN"
    private Timestamp createdAt;

    // ===== No-Arg Constructor =====
    public User() {
    }

    // ===== All-Arg Constructor =====
    public User(int userId, String fullName, String email, String password,
                String phone, String address, String role, Timestamp createdAt) {
        this.userId    = userId;
        this.fullName  = fullName;
        this.email     = email;
        this.password  = password;
        this.phone     = phone;
        this.address   = address;
        this.role      = role;
        this.createdAt = createdAt;
    }

    // ===== Getters & Setters =====

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // ===== Helper Methods =====

    /**
     * Returns {@code true} if this user holds the {@code "ADMIN"} role.
     *
     * @return {@code true} for admin users; {@code false} for customers
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }

    // ===== toString =====

    @Override
    public String toString() {
        return "User{"
                + "userId=" + userId
                + ", fullName='" + fullName + '\''
                + ", email='" + email + '\''
                + ", phone='" + phone + '\''
                + ", address='" + address + '\''
                + ", role='" + role + '\''
                + ", createdAt=" + createdAt
                + '}';
    }
}
