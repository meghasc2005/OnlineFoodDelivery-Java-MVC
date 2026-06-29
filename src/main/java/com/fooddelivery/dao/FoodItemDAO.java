package com.fooddelivery.dao;

import com.fooddelivery.model.FoodItem;
import java.sql.SQLException;
import java.util.List;

/**
 * FoodItemDAO — Data Access interface for {@link FoodItem} entity operations.
 *
 * <p>Provides full CRUD access to the {@code food_items} table,
 * plus search functionality for keyword-based food discovery.</p>
 */
public interface FoodItemDAO {

    /**
     * Returns all available food items for the given restaurant.
     *
     * @param restaurantId the restaurant's primary key
     * @return list of available {@link FoodItem} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<FoodItem> getFoodsByRestaurant(int restaurantId) throws SQLException;

    /**
     * Searches for food items by name or category keyword (LIKE match).
     *
     * @param keyword the search term (matched with {@code %keyword%})
     * @return list of matching {@link FoodItem} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<FoodItem> searchFoods(String keyword) throws SQLException;

    /**
     * Retrieves a single food item by its primary key.
     *
     * @param foodId the food item's primary key
     * @return the matching {@link FoodItem}, or {@code null} if not found
     * @throws SQLException on database access error
     */
    FoodItem getFoodById(int foodId) throws SQLException;

    /**
     * Inserts a new food item record.
     *
     * @param item the {@link FoodItem} to persist
     * @return {@code true} if the row was inserted successfully
     * @throws SQLException on database access error
     */
    boolean addFood(FoodItem item) throws SQLException;

    /**
     * Updates an existing food item record.
     *
     * @param item the {@link FoodItem} with updated fields
     * @return {@code true} if at least one row was updated
     * @throws SQLException on database access error
     */
    boolean updateFood(FoodItem item) throws SQLException;

    /**
     * Deletes a food item by its primary key.
     *
     * @param foodId the food item's primary key
     * @return {@code true} if at least one row was deleted
     * @throws SQLException on database access error
     */
    boolean deleteFood(int foodId) throws SQLException;

    /**
     * Returns all food items across all restaurants (admin use).
     *
     * @return list of all {@link FoodItem} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<FoodItem> getAllFoods() throws SQLException;
}
