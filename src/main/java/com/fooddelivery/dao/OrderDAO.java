package com.fooddelivery.dao;

import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import java.sql.SQLException;
import java.util.List;

/**
 * OrderDAO — Data Access interface for {@link Order} and {@link OrderItem} operations.
 *
 * <p>Handles order creation (using generated keys), order item insertion,
 * order retrieval by user or ID, and status updates for the admin panel.</p>
 */
public interface OrderDAO {

    /**
     * Inserts a new order record and returns the generated order ID.
     *
     * @param order the {@link Order} to persist (orderId field is ignored)
     * @return the generated {@code order_id} of the newly created order
     * @throws SQLException on database access error
     */
    int placeOrder(Order order) throws SQLException;

    /**
     * Inserts a single order item line record.
     *
     * @param item the {@link OrderItem} to persist
     * @return {@code true} if the row was inserted successfully
     * @throws SQLException on database access error
     */
    boolean addOrderItem(OrderItem item) throws SQLException;

    /**
     * Returns all orders for a given user, newest first.
     *
     * @param userId the user's primary key
     * @return list of {@link Order} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<Order> getOrdersByUser(int userId) throws SQLException;

    /**
     * Retrieves a single order by its primary key.
     *
     * @param orderId the order's primary key
     * @return the matching {@link Order}, or {@code null} if not found
     * @throws SQLException on database access error
     */
    Order getOrderById(int orderId) throws SQLException;

    /**
     * Returns all line items for the given order.
     *
     * @param orderId the order's primary key
     * @return list of {@link OrderItem} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<OrderItem> getOrderItems(int orderId) throws SQLException;

    /**
     * Returns all orders across all users, newest first (admin use).
     *
     * @return list of all {@link Order} objects (never {@code null})
     * @throws SQLException on database access error
     */
    List<Order> getAllOrders() throws SQLException;

    /**
     * Updates the status field of an existing order.
     *
     * @param orderId the order's primary key
     * @param status  the new status string
     * @return {@code true} if at least one row was updated
     * @throws SQLException on database access error
     */
    boolean updateOrderStatus(int orderId, String status) throws SQLException;
}
