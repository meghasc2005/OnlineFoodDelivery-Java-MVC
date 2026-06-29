package com.fooddelivery.dao;

import com.fooddelivery.model.User;
import java.sql.SQLException;

/**
 * UserDAO — Data Access interface for {@link User} entity operations.
 *
 * <p>All SQL for user persistence lives exclusively in the implementing class.
 * Methods declare {@link SQLException} to be caught and handled by the
 * Service layer.</p>
 */
public interface UserDAO {

    /**
     * Retrieves a user by their email address.
     *
     * @param email the user's email (unique)
     * @return the matching {@link User}, or {@code null} if not found
     * @throws SQLException on database access error
     */
    User getUserByEmail(String email) throws SQLException;

    /**
     * Retrieves a user by their primary key.
     *
     * @param userId the user's primary key
     * @return the matching {@link User}, or {@code null} if not found
     * @throws SQLException on database access error
     */
    User getUserById(int userId) throws SQLException;

    /**
     * Persists a new user registration record.
     *
     * @param user the {@link User} to insert (password must already be BCrypt-hashed)
     * @return {@code true} if the row was inserted successfully
     * @throws SQLException on database access error or duplicate email
     */
    boolean registerUser(User user) throws SQLException;

    /**
     * Updates the profile fields of an existing user.
     *
     * @param user the {@link User} with updated fullName, phone, and address
     * @return {@code true} if at least one row was updated
     * @throws SQLException on database access error
     */
    boolean updateUser(User user) throws SQLException;
}
