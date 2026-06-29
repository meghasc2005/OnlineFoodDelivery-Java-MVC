package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.OrderDAO;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDAOImpl — JDBC implementation of {@link OrderDAO}.
 *
 * <p>Handles order and order-item persistence. {@code placeOrder} uses
 * {@code RETURN_GENERATED_KEYS} to return the new order ID to the caller.
 * All operations use try-with-resources. No business logic resides here.</p>
 */
public class OrderDAOImpl implements OrderDAO {

    // ===== SQL Constants =====
    private static final String INSERT_ORDER =
            "INSERT INTO orders (user_id, total_amount, delivery_address, status, payment_method) "
            + "VALUES (?, ?, ?, ?, ?)";

    private static final String INSERT_ITEM =
            "INSERT INTO order_items (order_id, food_id, quantity, unit_price) "
            + "VALUES (?, ?, ?, ?)";

    private static final String FIND_BY_USER =
            "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC";

    private static final String FIND_BY_ID =
            "SELECT * FROM orders WHERE order_id = ?";

    private static final String GET_ITEMS =
            "SELECT * FROM order_items WHERE order_id = ?";

    private static final String GET_ALL =
            "SELECT * FROM orders ORDER BY created_at DESC";

    private static final String UPDATE_STATUS =
            "UPDATE orders SET status = ? WHERE order_id = ?";

    // ===== Private Row Mappers =====

    /**
     * Maps a single {@link ResultSet} row to an {@link Order} object.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a fully populated {@link Order}
     * @throws SQLException if any column read fails
     */
    private Order mapOrderRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderId(rs.getInt("order_id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setStatus(rs.getString("status"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setCreatedAt(rs.getTimestamp("created_at"));
        order.setUpdatedAt(rs.getTimestamp("updated_at"));
        return order;
    }

    /**
     * Maps a single {@link ResultSet} row to an {@link OrderItem} object.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a populated {@link OrderItem} (food field is null; set by service)
     * @throws SQLException if any column read fails
     */
    private OrderItem mapOrderItemRow(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setOrderItemId(rs.getInt("order_item_id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setFoodId(rs.getInt("food_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getDouble("unit_price"));
        return item;
    }

    // ===== DAO Method Implementations =====

    @Override
    public int placeOrder(Order order) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, order.getUserId());
            ps.setDouble(2, order.getTotalAmount());
            ps.setString(3, order.getDeliveryAddress());
            ps.setString(4, order.getStatus());
            ps.setString(5, order.getPaymentMethod());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return (int) keys.getLong(1);
                }
            }
        }
        throw new SQLException("Failed to place order — no generated key returned.");
    }

    @Override
    public boolean addOrderItem(OrderItem item) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_ITEM)) {

            ps.setInt(1, item.getOrderId());
            ps.setInt(2, item.getFoodId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<Order> getOrdersByUser(int userId) throws SQLException {
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_USER)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrderRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public Order getOrderById(int orderId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ID)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapOrderRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<OrderItem> getOrderItems(int orderId) throws SQLException {
        List<OrderItem> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ITEMS)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapOrderItemRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<Order> getAllOrders() throws SQLException {
        List<Order> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(GET_ALL);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapOrderRow(rs));
            }
        }
        return list;
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);

            return ps.executeUpdate() > 0;
        }
    }
}
