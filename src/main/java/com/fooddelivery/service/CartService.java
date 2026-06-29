package com.fooddelivery.service;

import com.fooddelivery.model.CartItem;
import java.util.List;

/**
 * CartService — Business logic interface for shopping cart operations.
 *
 * <p>Service methods do NOT declare {@link java.sql.SQLException}. All
 * database exceptions are caught internally and logged by the implementation.</p>
 */
public interface CartService {

    /**
     * Returns all cart items (with food details) for the given user.
     *
     * @param userId the user's primary key
     * @return list of {@link CartItem} objects with food populated;
     *         empty list if cart does not exist or on error
     */
    List<CartItem> getCartItems(int userId);

    /**
     * Adds a food item to the user's cart, creating the cart if needed.
     * If the item already exists, its quantity is incremented.
     *
     * @param userId   the user's primary key
     * @param foodId   the food item's primary key
     * @param quantity the quantity to add (must be &gt; 0)
     * @return {@code true} if the operation succeeded
     */
    boolean addToCart(int userId, int foodId, int quantity);

    /**
     * Updates the quantity of a specific cart item. If {@code quantity <= 0},
     * the item is removed instead.
     *
     * @param cartItemId the cart item's primary key
     * @param quantity   the new quantity
     * @return {@code true} if the operation succeeded
     */
    boolean updateQuantity(int cartItemId, int quantity);

    /**
     * Removes a single item from the cart.
     *
     * @param cartItemId the cart item's primary key
     * @return {@code true} if the delete succeeded
     */
    boolean removeItem(int cartItemId);

    /**
     * Calculates the total price of all cart items using each item's subtotal.
     *
     * @param items the list of {@link CartItem} objects (with food populated)
     * @return the sum of all subtotals; {@code 0.0} if list is null or empty
     */
    double calculateCartTotal(List<CartItem> items);

    /**
     * Removes all items from the user's cart (called after order placement).
     *
     * @param userId the user's primary key
     * @return {@code true} if cleared successfully or cart did not exist
     */
    boolean clearCart(int userId);

    /**
     * Returns the number of distinct items in the user's cart.
     *
     * @param userId the user's primary key
     * @return the item count; {@code 0} if cart is empty or does not exist
     */
    int getCartItemCount(int userId);
}
