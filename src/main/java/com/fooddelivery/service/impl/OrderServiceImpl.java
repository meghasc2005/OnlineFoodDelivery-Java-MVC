package com.fooddelivery.service.impl;

import com.fooddelivery.dao.FoodItemDAO;
import com.fooddelivery.dao.OrderDAO;
import com.fooddelivery.dao.impl.FoodItemDAOImpl;
import com.fooddelivery.dao.impl.OrderDAOImpl;
import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.FoodItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;
import com.fooddelivery.service.CartService;
import com.fooddelivery.service.OrderService;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderServiceImpl — Business logic implementation of {@link OrderService}.
 *
 * <p>Orchestrates the full order placement flow: reads cart items,
 * computes total, persists order + order items, and clears the cart.
 * Uses {@link CartService} (not CartDAO directly) for cart operations.
 * All {@link SQLException} instances are caught internally and logged.</p>
 */
public class OrderServiceImpl implements OrderService {

    // ===== DAO & Service Dependencies =====
    private final OrderDAO    orderDAO    = new OrderDAOImpl();
    private final FoodItemDAO foodItemDAO = new FoodItemDAOImpl();
    private final CartService cartService = new CartServiceImpl();

    // ===== Service Method Implementations =====

    @Override
    public Order placeOrder(int userId, String deliveryAddress, String paymentMethod) {
        // 1. Validate inputs
        if (deliveryAddress == null || deliveryAddress.trim().isEmpty()) {
            return null;
        }
        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            return null;
        }

        // 2. Read cart items via CartService
        List<CartItem> cartItems = cartService.getCartItems(userId);

        // 3. Reject empty cart
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty. Please add items before placing an order.");
        }

        // 4. Calculate total via CartService
        double total = cartService.calculateCartTotal(cartItems);

        // 5. Build the Order object
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(total);
        order.setDeliveryAddress(deliveryAddress.trim());
        order.setPaymentMethod(paymentMethod.trim());
        order.setStatus("PLACED");
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        order.setCreatedAt(now);
        order.setUpdatedAt(now);

        try {
            // 6. Persist order and get generated orderId
            int orderId = orderDAO.placeOrder(order);

            // 7. Persist each order item
            for (CartItem cartItem : cartItems) {
                OrderItem orderItem = new OrderItem();
                orderItem.setOrderId(orderId);
                orderItem.setFoodId(cartItem.getFoodId());
                orderItem.setQuantity(cartItem.getQuantity());
                // Use current food price as the locked-in unit price
                orderItem.setUnitPrice(cartItem.getFood().getPrice());
                orderDAO.addOrderItem(orderItem);
            }

            // 8. Clear cart after successful order
            cartService.clearCart(userId);

            // 9. Set the generated ID on the returned order
            order.setOrderId(orderId);

            // 10. Return the complete order
            return order;

        } catch (SQLException e) {
            System.err.println("[OrderServiceImpl] placeOrder error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Order> getUserOrders(int userId) {
        try {
            return orderDAO.getOrdersByUser(userId);
        } catch (SQLException e) {
            System.err.println("[OrderServiceImpl] getUserOrders error: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Order getOrderById(int orderId) {
        if (orderId <= 0) {
            return null;
        }
        try {
            return orderDAO.getOrderById(orderId);
        } catch (SQLException e) {
            System.err.println("[OrderServiceImpl] getOrderById error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<OrderItem> getOrderItemsWithFood(int orderId) {
        try {
            // 1. Fetch raw order items from DAO
            List<OrderItem> items = orderDAO.getOrderItems(orderId);

            // 2. Populate each item's transient FoodItem via FoodItemDAO
            for (OrderItem item : items) {
                FoodItem food = foodItemDAO.getFoodById(item.getFoodId());
                item.setFood(food);
            }

            return items;

        } catch (SQLException e) {
            System.err.println("[OrderServiceImpl] getOrderItemsWithFood error: "
                    + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Order> getAllOrdersForAdmin() {
        try {
            return orderDAO.getAllOrders();
        } catch (SQLException e) {
            System.err.println("[OrderServiceImpl] getAllOrdersForAdmin error: "
                    + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateOrderStatus(int orderId, String status) {
        if (orderId <= 0 || status == null || status.trim().isEmpty()) {
            return false;
        }
        try {
            return orderDAO.updateOrderStatus(orderId, status.trim());
        } catch (SQLException e) {
            System.err.println("[OrderServiceImpl] updateOrderStatus error: "
                    + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
