package com.fooddelivery.service.impl;

import com.fooddelivery.dao.FoodItemDAO;
import com.fooddelivery.dao.impl.FoodItemDAOImpl;
import com.fooddelivery.model.FoodItem;
import com.fooddelivery.service.FoodItemService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * FoodItemServiceImpl — Business logic implementation of {@link FoodItemService}.
 *
 * <p>Delegates persistence to {@link FoodItemDAO}. Performs validation before
 * DAO calls. All {@link SQLException} instances are caught internally
 * and logged; callers receive null/false/empty lists.</p>
 */
public class FoodItemServiceImpl implements FoodItemService {

    // ===== DAO Dependency =====
    private final FoodItemDAO foodItemDAO = new FoodItemDAOImpl();

    // ===== Service Method Implementations =====

    @Override
    public List<FoodItem> getFoodsByRestaurant(int restaurantId) {
        if (restaurantId <= 0) {
            return new ArrayList<>();
        }
        try {
            return foodItemDAO.getFoodsByRestaurant(restaurantId);
        } catch (SQLException e) {
            System.err.println("[FoodItemServiceImpl] getFoodsByRestaurant error: "
                    + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<FoodItem> searchFoods(String keyword) {
        // Blank keyword returns empty result immediately
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return foodItemDAO.searchFoods(keyword.trim());
        } catch (SQLException e) {
            System.err.println("[FoodItemServiceImpl] searchFoods error: "
                    + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public FoodItem getFoodById(int foodId) {
        if (foodId <= 0) {
            return null;
        }
        try {
            return foodItemDAO.getFoodById(foodId);
        } catch (SQLException e) {
            System.err.println("[FoodItemServiceImpl] getFoodById error: "
                    + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addFood(FoodItem item) {
        if (item == null) {
            return false;
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            return false;
        }
        if (item.getPrice() <= 0) {
            return false;
        }
        if (item.getRestaurantId() <= 0) {
            return false;
        }
        try {
            return foodItemDAO.addFood(item);
        } catch (SQLException e) {
            System.err.println("[FoodItemServiceImpl] addFood error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateFood(FoodItem item) {
        if (item == null || item.getFoodId() <= 0) {
            return false;
        }
        try {
            return foodItemDAO.updateFood(item);
        } catch (SQLException e) {
            System.err.println("[FoodItemServiceImpl] updateFood error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteFood(int foodId) {
        if (foodId <= 0) {
            return false;
        }
        try {
            return foodItemDAO.deleteFood(foodId);
        } catch (SQLException e) {
            System.err.println("[FoodItemServiceImpl] deleteFood error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<FoodItem> getAllFoodsForAdmin() {
        try {
            return foodItemDAO.getAllFoods();
        } catch (SQLException e) {
            System.err.println("[FoodItemServiceImpl] getAllFoodsForAdmin error: "
                    + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
