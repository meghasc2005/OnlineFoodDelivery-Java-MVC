package com.fooddelivery.service;

import com.fooddelivery.model.User;

/**
 * UserService — Business logic interface for user account operations.
 *
 * <p>Service methods do NOT declare {@link java.sql.SQLException}. All
 * database exceptions are caught internally and logged by the implementation.</p>
 */
public interface UserService {

    /**
     * Registers a new customer account after validation.
     *
     * @param fullName the user's display name
     * @param email    the user's email address (must be unique)
     * @param password the plain-text password (will be BCrypt-hashed)
     * @param phone    the user's phone number
     * @param address  the user's delivery address
     * @return {@code true} if registration succeeded; {@code false} on
     *         validation failure, duplicate email, or database error
     */
    boolean registerUser(String fullName, String email, String password,
                         String phone, String address);

    /**
     * Authenticates a user with their email and plain-text password.
     *
     * @param email    the user's email address
     * @param password the plain-text password to verify
     * @return the authenticated {@link User} object, or {@code null} if
     *         credentials are invalid or a database error occurs
     */
    User loginUser(String email, String password);

    /**
     * Updates the profile fields (name, phone, address) of an existing user.
     *
     * @param user the {@link User} with updated fields and a valid userId
     * @return {@code true} if the update succeeded
     */
    boolean updateProfile(User user);
}
