package com.fooddelivery.dao;

import com.fooddelivery.model.Payment;
import java.sql.SQLException;

/**
 * PaymentDAO — Data Access interface for {@link Payment} entity operations.
 *
 * <p>Manages the {@code payments} table. Each order has exactly one payment
 * record (enforced by the UNIQUE constraint on {@code order_id}).</p>
 */
public interface PaymentDAO {

    /**
     * Inserts a new payment record.
     *
     * @param payment the {@link Payment} to persist
     * @return {@code true} if the row was inserted successfully
     * @throws SQLException on database access error
     */
    boolean savePayment(Payment payment) throws SQLException;

    /**
     * Retrieves the payment record for a given order.
     *
     * @param orderId the order's primary key
     * @return the matching {@link Payment}, or {@code null} if not found
     * @throws SQLException on database access error
     */
    Payment getPaymentByOrderId(int orderId) throws SQLException;

    /**
     * Updates the status of an existing payment record.
     *
     * @param paymentId the payment's primary key
     * @param status    the new status string ({@code "PENDING"}, {@code "SUCCESS"}, or {@code "FAILED"})
     * @return {@code true} if at least one row was updated
     * @throws SQLException on database access error
     */
    boolean updatePaymentStatus(int paymentId, String status) throws SQLException;
}
