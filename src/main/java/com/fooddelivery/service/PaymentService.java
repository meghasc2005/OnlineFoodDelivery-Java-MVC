package com.fooddelivery.service;

import com.fooddelivery.model.Payment;

/**
 * PaymentService — Business logic interface for payment processing.
 *
 * <p>Service methods do NOT declare {@link java.sql.SQLException}. All
 * database exceptions are caught internally and logged by the implementation.</p>
 */
public interface PaymentService {

    /**
     * Processes a payment for the given order and persists the result.
     * On {@code SUCCESS}, the order status is updated to {@code "CONFIRMED"}.
     *
     * <ul>
     *   <li>{@code COD}  — always succeeds; transaction ID is {@code "COD-{orderId}"}</li>
     *   <li>{@code UPI}  — succeeds if {@code transactionInput} is non-blank</li>
     *   <li>{@code CARD} — dummy success with a timestamped transaction ID</li>
     * </ul>
     *
     * @param orderId          the order's primary key
     * @param userId           the paying user's primary key
     * @param amount           the amount to charge
     * @param method           the payment method ({@code COD}, {@code UPI}, or {@code CARD})
     * @param transactionInput the UPI ID entered by the user (may be null for COD/CARD)
     * @return the persisted {@link Payment} record, or {@code null} on error
     */
    Payment processPayment(int orderId, int userId, double amount,
                           String method, String transactionInput);

    /**
     * Returns the payment record for the given order.
     *
     * @param orderId the order's primary key
     * @return the {@link Payment}, or {@code null} if not found or on error
     */
    Payment getPaymentForOrder(int orderId);
}
