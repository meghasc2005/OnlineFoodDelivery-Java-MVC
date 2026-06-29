package com.fooddelivery.dao;

import com.fooddelivery.model.Cart;
import com.fooddelivery.model.CartItem;
import java.sql.SQLException;
import java.util.List;

/**
 * CartDAO — Data Access interface for {@link Cart} and {@link CartItem} operations.
 *
 * <p>Manages the {@code cart} and {@code cart_items} tables. Cart items use an
 * {@code ON DUPLICATE KEY UPDATE} strategy so adding the same food item twice
 * increments the quantity rather than creating a duplicate row.</p>
 */
public interface CartDAO {

    /**
     * Retrieves the cart record for a given user.
     *
     * @param userId the user's primary key
     * @return the user's {@link Cart}, or {@code null} if no cart exists
     * @throws SQLException on database access error
     */
    Cart getCartByUserId(int userId) throws SQLException;

    /**
     * Creates a new cart for the given user and returns the generated cart ID.
     *
     * @param userId the user's primary key
     * @return the generated {@code cart_id} of the new cart
     * @throws SQLException on database access error
     */
    int createCart(int userId) throws SQLException;

    /**
     * Adds a food item to the cart. If the item already exists, its quantity
     * is incremented by {@code qty} (via {@code ON DUPLICATE KEY UPDATE}).
     *
     * @param cartId the cart's primary key
     * @param foodId the food item's primary key
     * @param qty    the quantity to add
     * @return {@code true} if the operation succeeded
     * @throws SQLException on database access error
     */
    boolean addItemToCart(int cartId, int foodId, int qty) throws SQLException;

    /**
     * Sets the quantity of an existing cart item to the given value.
     *
     * @param cartItemId the cart item's primary key
     * @param qty        the new quantity
     * @return {@code true} if at least one row was updated
     * @throws SQLException on database access error
     */
    boolean updateCartItemQuantity(int cartItemId, int qty) throws SQLException;

    /**
     * Removes a single item from the cart.
     *
     * @param cartItemId the cart item's primary key
     * @return {@code true} if at least one row was deleted
     * @throws SQLException on database access error
     */
    boolean removeCartItem(int cartItemId) throws SQLException;

    /**
     * Returns all cart items for the given cart, with each item's
     * {@link com.fooddelivery.model.FoodItem} detail pre-populated via JOIN.
     *
     * @param cartId the cart's primary key
     * @return list of {@link CartItem} objects with food details (never {@code null})
     * @throws SQLException on database access error
     */
    List<CartItem> getCartItemsWithFood(int cartId) throws SQLException;

    /**
     * Removes all items from the cart (called after order placement).
     *
     * @param cartId the cart's primary key
     * @return {@code true} if rows were deleted (or cart was already empty)
     * @throws SQLException on database access error
     */
    boolean clearCart(int cartId) throws SQLException;
}
