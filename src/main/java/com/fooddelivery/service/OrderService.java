package com.fooddelivery.service;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import java.util.List;

/**
 * OrderService — Business logic interface for order lifecycle operations.
 *
 * <p>Service methods do NOT declare {@link java.sql.SQLException}. All
 * database exceptions are caught internally and logged by the implementation.</p>
 */
public interface OrderService {

    /**
     * Places a new order by reading the user's cart, creating the order record,
     * persisting all order items, and clearing the cart on success.
     *
     * @param userId          the ordering user's primary key
     * @param deliveryAddress the delivery address for this order
     * @param paymentMethod   the selected payment method (COD, UPI, or CARD)
     * @return the fully populated {@link Order} with the generated order ID,
     *         or {@code null} on failure
     */
    Order placeOrder(int userId, String deliveryAddress, String paymentMethod);

    /**
     * Returns all orders for the given user, newest first.
     *
     * @param userId the user's primary key
     * @return list of {@link Order} objects; empty list on error
     */
    List<Order> getUserOrders(int userId);

    /**
     * Returns a single order by its primary key.
     *
     * @param orderId the order's primary key
     * @return the {@link Order}, or {@code null} if not found or on error
     */
    Order getOrderById(int orderId);

    /**
     * Returns all order items for a given order, with each item's
     * {@link com.fooddelivery.model.FoodItem} detail pre-populated.
     *
     * @param orderId the order's primary key
     * @return list of {@link OrderItem} objects with food populated;
     *         empty list on error
     */
    List<OrderItem> getOrderItemsWithFood(int orderId);

    /**
     * Returns all orders across all users (admin use), newest first.
     *
     * @return list of all {@link Order} objects; empty list on error
     */
    List<Order> getAllOrdersForAdmin();

    /**
     * Updates the status of an existing order.
     *
     * @param orderId the order's primary key
     * @param status  the new status string
     * @return {@code true} if the update succeeded
     */
    boolean updateOrderStatus(int orderId, String status);
}
