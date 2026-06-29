package com.fooddelivery.service.impl;

import com.fooddelivery.dao.CartDAO;
import com.fooddelivery.dao.impl.CartDAOImpl;
import com.fooddelivery.model.Cart;
import com.fooddelivery.model.CartItem;
import com.fooddelivery.service.CartService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * CartServiceImpl — Business logic implementation of {@link CartService}.
 *
 * <p>Manages the user's cart lifecycle: auto-creating the cart on first add,
 * delegating item operations to {@link CartDAO}, and computing totals.
 * All {@link SQLException} instances are caught internally and logged.</p>
 */
public class CartServiceImpl implements CartService {

    // ===== DAO Dependency =====
    private final CartDAO cartDAO = new CartDAOImpl();

    // ===== Service Method Implementations =====

    @Override
    public List<CartItem> getCartItems(int userId) {
        try {
            // 1. Fetch cart for this user
            Cart cart = cartDAO.getCartByUserId(userId);

            // 2. No cart exists yet → empty list
            if (cart == null) {
                return new ArrayList<>();
            }

            // 3. Return items from JOIN query
            return cartDAO.getCartItemsWithFood(cart.getCartId());

        } catch (SQLException e) {
            System.err.println("[CartServiceImpl] getCartItems error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean addToCart(int userId, int foodId, int quantity) {
        // 1. Validate quantity
        if (quantity <= 0) {
            return false;
        }

        try {
            // 2. Find or create the cart
            Cart cart = cartDAO.getCartByUserId(userId);
            int cartId;
            if (cart == null) {
                // 3a. Cart does not exist — create one
                cartId = cartDAO.createCart(userId);
            } else {
                // 3b. Cart already exists
                cartId = cart.getCartId();
            }

            // 4. Add item (ON DUPLICATE KEY UPDATE handles re-adding)
            return cartDAO.addItemToCart(cartId, foodId, quantity);

        } catch (SQLException e) {
            System.err.println("[CartServiceImpl] addToCart error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateQuantity(int cartItemId, int quantity) {
        // If quantity drops to zero or below, remove the item instead
        if (quantity <= 0) {
            return removeItem(cartItemId);
        }
        try {
            return cartDAO.updateCartItemQuantity(cartItemId, quantity);
        } catch (SQLException e) {
            System.err.println("[CartServiceImpl] updateQuantity error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeItem(int cartItemId) {
        try {
            return cartDAO.removeCartItem(cartItemId);
        } catch (SQLException e) {
            System.err.println("[CartServiceImpl] removeItem error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double calculateCartTotal(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return 0.0;
        }
        return items.stream()
                    .mapToDouble(CartItem::getSubtotal)
                    .sum();
    }

    @Override
    public boolean clearCart(int userId) {
        try {
            Cart cart = cartDAO.getCartByUserId(userId);
            if (cart == null) {
                return true; // Nothing to clear
            }
            return cartDAO.clearCart(cart.getCartId());
        } catch (SQLException e) {
            System.err.println("[CartServiceImpl] clearCart error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getCartItemCount(int userId) {
        List<CartItem> items = getCartItems(userId);
        return items.size();
    }
}
