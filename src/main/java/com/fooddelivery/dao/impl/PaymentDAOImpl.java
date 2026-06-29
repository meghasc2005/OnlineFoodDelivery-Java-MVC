package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.PaymentDAO;
import com.fooddelivery.model.Payment;
import com.fooddelivery.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PaymentDAOImpl — JDBC implementation of {@link PaymentDAO}.
 *
 * <p>Manages the {@code payments} table. Each order has exactly one payment
 * record (enforced by the UNIQUE constraint on {@code order_id} in the schema).
 * All operations use try-with-resources. No business logic resides here.</p>
 */
public class PaymentDAOImpl implements PaymentDAO {

    // ===== SQL Constants =====
    private static final String INSERT_PAYMENT =
            "INSERT INTO payments (order_id, user_id, amount, payment_method, transaction_id, status) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String FIND_BY_ORDER =
            "SELECT * FROM payments WHERE order_id = ?";

    private static final String UPDATE_STATUS =
            "UPDATE payments SET status = ? WHERE payment_id = ?";

    // ===== Private Row Mapper =====

    /**
     * Maps a single {@link ResultSet} row to a {@link Payment} object.
     *
     * @param rs an open {@link ResultSet} positioned at the current row
     * @return a fully populated {@link Payment}
     * @throws SQLException if any column read fails
     */
    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment payment = new Payment();
        payment.setPaymentId(rs.getInt("payment_id"));
        payment.setOrderId(rs.getInt("order_id"));
        payment.setUserId(rs.getInt("user_id"));
        payment.setAmount(rs.getDouble("amount"));
        payment.setPaymentMethod(rs.getString("payment_method"));
        payment.setTransactionId(rs.getString("transaction_id"));
        payment.setStatus(rs.getString("status"));
        payment.setPaidAt(rs.getTimestamp("paid_at"));
        return payment;
    }

    // ===== DAO Method Implementations =====

    @Override
    public boolean savePayment(Payment payment) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_PAYMENT)) {

            ps.setInt(1, payment.getOrderId());
            ps.setInt(2, payment.getUserId());
            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getPaymentMethod());
            ps.setString(5, payment.getTransactionId()); // nullable — sets NULL if null
            ps.setString(6, payment.getStatus());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Payment getPaymentByOrderId(int orderId) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_ORDER)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    @Override
    public boolean updatePaymentStatus(int paymentId, String status) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_STATUS)) {

            ps.setString(1, status);
            ps.setInt(2, paymentId);

            return ps.executeUpdate() > 0;
        }
    }
}
