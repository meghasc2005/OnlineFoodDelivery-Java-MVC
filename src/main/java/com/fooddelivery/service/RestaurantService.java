package com.fooddelivery.service;

import com.fooddelivery.model.Restaurant;
import java.util.List;

/**
 * RestaurantService — Business logic interface for restaurant operations.
 *
 * <p>Service methods do NOT declare {@link java.sql.SQLException}. All
 * database exceptions are caught internally and logged by the implementation.</p>
 */
public interface RestaurantService {

    /**
     * Returns all restaurants currently marked as active (customer-facing).
     *
     * @return list of active restaurants; empty list on error
     */
    List<Restaurant> getAllActiveRestaurants();

    /**
     * Returns all restaurants regardless of status (admin use).
     *
     * @return list of all restaurants; empty list on error
     */
    List<Restaurant> getAllRestaurantsForAdmin();

    /**
     * Returns a single restaurant by its primary key.
     *
     * @param id the restaurant's primary key
     * @return the {@link Restaurant}, or {@code null} if not found or on error
     */
    Restaurant getRestaurantById(int id);

    /**
     * Adds a new restaurant record after validating required fields.
     *
     * @param r the restaurant to persist
     * @return {@code true} if the insert succeeded
     */
    boolean addRestaurant(Restaurant r);

    /**
     * Updates an existing restaurant record.
     *
     * @param r the restaurant with updated fields
     * @return {@code true} if the update succeeded
     */
    boolean updateRestaurant(Restaurant r);

    /**
     * Deletes a restaurant by its primary key.
     *
     * @param id the restaurant's primary key
     * @return {@code true} if the delete succeeded
     */
    boolean deleteRestaurant(int id);
}
