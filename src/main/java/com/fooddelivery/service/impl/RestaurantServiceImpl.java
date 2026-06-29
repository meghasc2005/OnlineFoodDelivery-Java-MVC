package com.fooddelivery.service.impl;

import com.fooddelivery.dao.RestaurantDAO;
import com.fooddelivery.dao.impl.RestaurantDAOImpl;
import com.fooddelivery.model.Restaurant;
import com.fooddelivery.service.RestaurantService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * RestaurantServiceImpl — Business logic implementation of {@link RestaurantService}.
 *
 * <p>Delegates persistence to {@link RestaurantDAO}. Performs null/validation
 * checks before DAO calls. All {@link SQLException} instances are caught
 * internally and logged; callers receive null/false/empty lists.</p>
 */
public class RestaurantServiceImpl implements RestaurantService {

    // ===== DAO Dependency =====
    private final RestaurantDAO restaurantDAO = new RestaurantDAOImpl();

    // ===== Service Method Implementations =====

    @Override
    public List<Restaurant> getAllActiveRestaurants() {
        try {
            return restaurantDAO.getAllActiveRestaurants();
        } catch (SQLException e) {
            System.err.println("[RestaurantServiceImpl] getAllActiveRestaurants error: "
                    + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Restaurant> getAllRestaurantsForAdmin() {
        try {
            return restaurantDAO.getAllRestaurants();
        } catch (SQLException e) {
            System.err.println("[RestaurantServiceImpl] getAllRestaurantsForAdmin error: "
                    + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Restaurant getRestaurantById(int id) {
        if (id <= 0) {
            return null;
        }
        try {
            return restaurantDAO.getRestaurantById(id);
        } catch (SQLException e) {
            System.err.println("[RestaurantServiceImpl] getRestaurantById error: "
                    + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addRestaurant(Restaurant r) {
        if (r == null) {
            return false;
        }
        if (r.getName() == null || r.getName().trim().isEmpty()) {
            return false;
        }
        try {
            return restaurantDAO.addRestaurant(r);
        } catch (SQLException e) {
            System.err.println("[RestaurantServiceImpl] addRestaurant error: "
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateRestaurant(Restaurant r) {
        if (r == null || r.getRestaurantId() <= 0) {
            return false;
        }
        try {
            return restaurantDAO.updateRestaurant(r);
        } catch (SQLException e) {
            System.err.println("[RestaurantServiceImpl] updateRestaurant error: "
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteRestaurant(int id) {
        if (id <= 0) {
            return false;
        }
        try {
            return restaurantDAO.deleteRestaurant(id);
        } catch (SQLException e) {
            System.err.println("[RestaurantServiceImpl] deleteRestaurant error: "
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
