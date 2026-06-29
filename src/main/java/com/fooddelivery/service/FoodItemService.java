package com.fooddelivery.service;

import com.fooddelivery.model.FoodItem;
import java.util.List;

/**
 * FoodItemService — Business logic interface for food item operations.
 *
 * <p>Service methods do NOT declare {@link java.sql.SQLException}. All
 * database exceptions are caught internally and logged by the implementation.</p>
 */
public interface FoodItemService {

    /**
     * Returns all available food items for the given restaurant.
     *
     * @param restaurantId the restaurant's primary key
     * @return list of available food items; empty list on error
     */
    List<FoodItem> getFoodsByRestaurant(int restaurantId);

    /**
     * Searches food items by name or category using a keyword.
     *
     * @param keyword the search term; blank keyword returns empty list
     * @return list of matching food items; empty list if no results or on error
     */
    List<FoodItem> searchFoods(String keyword);

    /**
     * Returns a single food item by its primary key.
     *
     * @param foodId the food item's primary key
     * @return the {@link FoodItem}, or {@code null} if not found or on error
     */
    FoodItem getFoodById(int foodId);

    /**
     * Adds a new food item after validating required fields.
     *
     * @param item the food item to persist
     * @return {@code true} if the insert succeeded
     */
    boolean addFood(FoodItem item);

    /**
     * Updates an existing food item record.
     *
     * @param item the food item with updated fields
     * @return {@code true} if the update succeeded
     */
    boolean updateFood(FoodItem item);

    /**
     * Deletes a food item by its primary key.
     *
     * @param foodId the food item's primary key
     * @return {@code true} if the delete succeeded
     */
    boolean deleteFood(int foodId);

    /**
     * Returns all food items across all restaurants (admin use).
     *
     * @return list of all food items; empty list on error
     */
    List<FoodItem> getAllFoodsForAdmin();
}
