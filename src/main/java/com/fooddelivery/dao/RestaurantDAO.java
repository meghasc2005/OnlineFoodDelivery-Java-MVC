package com.fooddelivery.dao;

import com.fooddelivery.model.Restaurant;
import java.sql.SQLException;
import java.util.List;

/**
 * RestaurantDAO — Data Access interface for {@link Restaurant} entity operations.
 *
 * <p>Provides read and write access to the {@code restaurants} table.
 * All SQL lives in the implementing class; this interface enforces the contract.</p>
 */
public interface RestaurantDAO {

    /**
     * Returns all restaurants where {@code is_active = true}, ordered by name.
     *
     * @return list of active {@link Restaurant} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<Restaurant> getAllActiveRestaurants() throws SQLException;

    /**
     * Returns all restaurants regardless of active status (admin use).
     *
     * @return list of all {@link Restaurant} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<Restaurant> getAllRestaurants() throws SQLException;

    /**
     * Retrieves a restaurant by its primary key.
     *
     * @param id the restaurant's primary key
     * @return the matching {@link Restaurant}, or {@code null} if not found
     * @throws SQLException on database access error
     */
    Restaurant getRestaurantById(int id) throws SQLException;

    /**
     * Inserts a new restaurant record.
     *
     * @param r the {@link Restaurant} to persist
     * @return {@code true} if the row was inserted successfully
     * @throws SQLException on database access error
     */
    boolean addRestaurant(Restaurant r) throws SQLException;

    /**
     * Updates an existing restaurant record.
     *
     * @param r the {@link Restaurant} with updated fields
     * @return {@code true} if at least one row was updated
     * @throws SQLException on database access error
     */
    boolean updateRestaurant(Restaurant r) throws SQLException;

    /**
     * Deletes a restaurant by its primary key.
     *
     * @param id the restaurant's primary key
     * @return {@code true} if at least one row was deleted
     * @throws SQLException on database access error
     */
    boolean deleteRestaurant(int id) throws SQLException;
}
