package com.tka.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.tka.entity.CustomerOrder;
import com.tka.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // ============================================================
    // API 1 : Place Order
    // POST /api/orders
    // ============================================================

    public Map<String, Object> placeOrder(CustomerOrder order) {

        // Customer name validation
        if (order.getCustomerName() == null ||
                order.getCustomerName().trim().isEmpty()) {

            return errorResponse("Customer name cannot be empty");
        }

        // Quantity validation
        if (order.getQuantity() <= 0) {

            return errorResponse("Quantity must be greater than 0");
        }

        // Price validation
        if (order.getPricePerUnit() <= 0) {

            return errorResponse("Price per unit must be greater than 0");
        }

        // Discount validation
        if (order.getDiscountPercent() < 0 ||
                order.getDiscountPercent() > 30) {

            return errorResponse(
                    "Discount percentage must be between 0 and 30");
        }

        // Payment mode validation
        String paymentMode = order.getPaymentMode();

        if (paymentMode == null ||
                !(paymentMode.equalsIgnoreCase("UPI")
                        || paymentMode.equalsIgnoreCase("Card")
                        || paymentMode.equalsIgnoreCase("Cash"))) {

            return errorResponse(
                    "Payment mode must be UPI, Card or Cash");
        }

        // Status validation
        String status = order.getOrderStatus();

        if (status == null || status.trim().isEmpty()) {
            order.setOrderStatus("Placed");
        }

        // Save order
        CustomerOrder savedOrder = orderRepository.save(order);

        // Gross amount
        double grossAmount =
                savedOrder.getQuantity()
                        * savedOrder.getPricePerUnit();

        // Discount amount
        double discountAmount =
                grossAmount
                        * savedOrder.getDiscountPercent()
                        / 100;

        // Final amount
        double finalAmount =
                grossAmount - discountAmount;

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("message", "Order placed successfully");
        response.put("orderId", savedOrder.getOrderId());
        response.put("customerName",
                savedOrder.getCustomerName());
        response.put("grossAmount", grossAmount);
        response.put("discountAmount", discountAmount);
        response.put("finalAmount", finalAmount);

        return response;
    }


    // ============================================================
    // API 2 : Get Orders Above Final Amount
    // GET /api/orders/above/{amount}
    // ============================================================

    public List<Map<String, Object>> getOrdersAbove(double amount) {

        List<CustomerOrder> orders =
                orderRepository.findAll();

        List<Map<String, Object>> result =
                new ArrayList<>();

        for (CustomerOrder order : orders) {

            double grossAmount =
                    order.getQuantity()
                            * order.getPricePerUnit();

            double discountAmount =
                    grossAmount
                            * order.getDiscountPercent()
                            / 100;

            double finalAmount =
                    grossAmount - discountAmount;

            if (finalAmount > amount) {

                Map<String, Object> data =
                        new LinkedHashMap<>();

                data.put("orderId",
                        order.getOrderId());

                data.put("customerName",
                        order.getCustomerName());

                data.put("productName",
                        order.getProductName());

                data.put("quantity",
                        order.getQuantity());

                data.put("grossAmount",
                        grossAmount);

                data.put("discountAmount",
                        discountAmount);

                data.put("finalAmount",
                        finalAmount);

                result.add(data);
            }
        }

        return result;
    }


    // ============================================================
    // API 3 : Update Order Status
    // PUT /api/orders/{orderId}/status
    // ============================================================

    public Map<String, Object> updateStatus(
            int orderId,
            String requestedStatus) {

        CustomerOrder order =
                orderRepository.findById(orderId).orElse(null);

        if (order == null) {

            return errorResponse("Order not found");
        }

        String currentStatus =
                order.getOrderStatus();

        if (requestedStatus == null ||
                requestedStatus.trim().isEmpty()) {

            return errorResponse("Status cannot be empty");
        }

        requestedStatus =
                requestedStatus.trim();

        // Check whether transition is valid
        boolean validTransition =
                isValidTransition(
                        currentStatus,
                        requestedStatus);

        if (!validTransition) {

            Map<String, Object> response =
                    new LinkedHashMap<>();

            response.put(
                    "message",
                    "Invalid order status transition");

            response.put(
                    "currentStatus",
                    currentStatus);

            response.put(
                    "requestedStatus",
                    requestedStatus);

            return response;
        }

        // Update status
        order.setOrderStatus(requestedStatus);

        orderRepository.save(order);

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "message",
                "Order status updated successfully");

        response.put(
                "orderId",
                order.getOrderId());

        response.put(
                "oldStatus",
                currentStatus);

        response.put(
                "newStatus",
                requestedStatus);

        return response;
    }


    // Status transition business rule
    private boolean isValidTransition(
            String currentStatus,
            String requestedStatus) {

        if (currentStatus.equalsIgnoreCase("Placed")) {

            return requestedStatus.equalsIgnoreCase("Shipped")
                    || requestedStatus.equalsIgnoreCase("Cancelled");
        }

        if (currentStatus.equalsIgnoreCase("Shipped")) {

            return requestedStatus.equalsIgnoreCase("Delivered");
        }

        // Delivered cannot be changed
        if (currentStatus.equalsIgnoreCase("Delivered")) {

            return false;
        }

        // Cancelled cannot be changed
        if (currentStatus.equalsIgnoreCase("Cancelled")) {

            return false;
        }

        return false;
    }


    // ============================================================
    // API 4 : Category Sales Summary
    // GET /api/orders/summary/{category}
    // ============================================================

    public Map<String, Object> categorySummary(
            String category) {

        List<CustomerOrder> orders =
                orderRepository.findAll();

        int totalOrders = 0;
        int deliveredOrders = 0;
        int placedOrders = 0;
        int cancelledOrders = 0;

        int totalQuantity = 0;

        double totalRevenue = 0;

        double highestValueOrder = 0;

        String highestValueCustomer = null;

        for (CustomerOrder order : orders) {

            if (!order.getCategory()
                    .equalsIgnoreCase(category)) {

                continue;
            }

            totalOrders++;

            String status =
                    order.getOrderStatus();

            if (status.equalsIgnoreCase("Delivered")) {
                deliveredOrders++;
            }

            if (status.equalsIgnoreCase("Placed")) {
                placedOrders++;
            }

            if (status.equalsIgnoreCase("Cancelled")) {
                cancelledOrders++;
            }

            totalQuantity += order.getQuantity();

            double grossAmount =
                    order.getQuantity()
                            * order.getPricePerUnit();

            double discountAmount =
                    grossAmount
                            * order.getDiscountPercent()
                            / 100;

            double finalAmount =
                    grossAmount - discountAmount;

            // Cancelled orders excluded from revenue
            if (!status.equalsIgnoreCase("Cancelled")) {

                totalRevenue += finalAmount;
            }

            // Highest value order
            if (finalAmount > highestValueOrder) {

                highestValueOrder = finalAmount;

                highestValueCustomer =
                        order.getCustomerName();
            }
        }

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("category", category);
        response.put("totalOrders", totalOrders);
        response.put("deliveredOrders", deliveredOrders);
        response.put("placedOrders", placedOrders);
        response.put("cancelledOrders", cancelledOrders);
        response.put("totalQuantity", totalQuantity);
        response.put("totalRevenue", totalRevenue);
        response.put("highestValueOrder",
                highestValueOrder);
        response.put("highestValueCustomer",
                highestValueCustomer);

        return response;
    }


    // Common error response

    private Map<String, Object> errorResponse(
            String message) {

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("message", message);

        return response;
    }
}