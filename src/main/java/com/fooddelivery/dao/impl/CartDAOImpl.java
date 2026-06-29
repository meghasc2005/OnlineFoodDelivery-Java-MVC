package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.CartDAO;
import com.fooddelivery.model.Cart;
import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.FoodItem;
import com.fooddelivery.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * CartDAOImpl — JDBC implementation of {@link CartDAO}.
 *
 * <p>Manages the {@code cart} and {@code cart_items} tables.
 * The {@code ADD_ITEM} SQL uses {@code ON DUPLICATE KEY UPDATE} so that
 * adding an already-present food item simply increments its quantity.
 * {@code createCart} returns the auto-generated cart ID.</p>
 */
public class CartDAOImpl implements CartDAO {

    // ===== SQL Constants =====
    private static final String FIND_CART_BY_USER =
            "SELECT * FROM cart WHERE user_id = ?";

    private static final String CREATE_CART =
            "INSERT INTO cart (user_id) VALUES (?)";

    private static final String ADD_ITEM =
            "INSERT INTO cart_items (cart_id, food_id, quantity) "
            + "VALUES (?, ?, ?) "
            + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

    private static final String UPDATE_QTY =
            "UPDATE cart_items SET quantity = ? WHERE cart_item_id = ?";

    private static final String REMOVE_ITEM =
            "DELETE FROM cart_items WHERE cart_item_id = ?";

    private static final String GET_ITEMS_WITH_FOOD =
            "SELECT ci.cart_item_id, ci.cart_id, ci.quantity, "
            + "       fi.food_id, fi.restaurant_id, fi.name, fi.description, "
            + "       fi.price, fi.category, fi.image_url, fi.is_available "
            + "FROM cart_items ci "
            + "JOIN food_items fi ON ci.food_id = fi.food_id "
            + "WHERE ci.cart_id = ?";

    private static final String CLEAR_CART =
            "DELETE FROM cart_items WHERE cart_id = ?";

    // ===== Private Row Mappers =====

    /**
     * Maps a {@link ResultSet} row to a {@link Cart} object.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a populated {@link Cart}
     * @throws SQLException if any column read fails
     */
    private Cart mapCartRow(ResultSet rs) throws SQLException {
        Cart cart = new Cart();
        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setCreatedAt(rs.getTimestamp("created_at"));
        return cart;
    }

    /**
     * Maps a JOIN result row to a {@link CartItem} with its embedded {@link FoodItem}.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a {@link CartItem} with {@code food} field populated
     * @throws SQLException if any column read fails
     */
    private CartItem mapCartItemWithFood(ResultSet rs) throws SQLException {
        // Map CartItem persisted fields
        CartItem cartItem = new CartItem();
        cartItem.setCartItemId(rs.getInt("cart_item_id"));
        cartItem.setCartId(rs.getInt("cart_id"));
        cartItem.setQuantity(rs.getInt("quantity"));

        // Map embedded FoodItem from JOIN
        FoodItem food = new FoodItem();
        food.setFoodId(rs.getInt("food_id"));
        food.setRestaurantId(rs.getInt("restaurant_id"));
        food.setName(rs.getString("name"));
        food.setDescription(rs.getString("description"));
        food.setPrice(rs.getDouble("price"));
        food.setCategory(rs.getString("category"));
        food.setImageUrl(rs.getString("image_url"));
        food.setAvailable(rs.getBoolean("is_available"));

        cartItem.setFoodId(food.getFoodId());
        cartItem.setFood(food);
        return cartItem;
    }

    // ===== DAO Method Implementations =====

    @Override
    public Cart getCartByUserId(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_CART_BY_USER)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapCartRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public int createCart(int userId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     CREATE_CART, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, userId);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return (int) keys.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to create cart — no generated key returned.");
    }

    @Override
    public boolean addItemToCart(int cartId, int foodId, int qty) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(ADD_ITEM)) {

            ps.setInt(1, cartId);
            ps.setInt(2, foodId);
            ps.setInt(3, qty);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateCartItemQuantity(int cartItemId, int qty) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_QTY)) {

            ps.setInt(1, qty);
            ps.setInt(2, cartItemId);

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean removeCartItem(int cartItemId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(REMOVE_ITEM)) {

            ps.setInt(1, cartItemId);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<CartItem> getCartItemsWithFood(int cartId) throws SQLException {
        List<CartItem> items = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ITEMS_WITH_FOOD)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(mapCartItemWithFood(rs));
                }
            }
        }
        return items;
    }

    @Override
    public boolean clearCart(int cartId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CLEAR_CART)) {

            ps.setInt(1, cartId);
            ps.executeUpdate();
            return true;
        }
    }
}
