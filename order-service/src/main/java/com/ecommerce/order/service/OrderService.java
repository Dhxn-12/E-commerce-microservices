package com.ecommerce.order.service;

import com.ecommerce.order.client.InventoryClient;
import com.ecommerce.order.client.PaymentClient;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderItemRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.model.Order;
import com.ecommerce.order.model.OrderItem;
import com.ecommerce.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;

    public OrderService(OrderRepository orderRepository,
                        InventoryClient inventoryClient,
                        PaymentClient paymentClient) {
        this.orderRepository  = orderRepository;
        this.inventoryClient  = inventoryClient;
        this.paymentClient    = paymentClient;
    }

    /**
     * Full order placement flow:
     * 1. Check stock for every item via Inventory Service
     * 2. Calculate total amount
     * 3. Process payment via Payment Service
     * 4. If payment success → reduce stock + save order as CONFIRMED
     * 5. If payment fails → save order as PAYMENT_FAILED
     */
    @Transactional
    public OrderResponse placeOrder(CreateOrderRequest request) {

        // Step 1: Check stock for all items
        for (OrderItemRequest item : request.getItems()) {
            Map<String, Object> stockCheck = inventoryClient.checkStock(
                item.getProductId(), item.getQuantity()
            );
            boolean available = (Boolean) stockCheck.get("available");
            if (!available) {
                throw new RuntimeException(
                    "Insufficient stock for product: " + item.getProductName()
                    + ". " + stockCheck.get("message")
                );
            }
        }

        // Step 2: Calculate total amount
        BigDecimal totalAmount = request.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Step 3: Build and save initial order (PENDING)
        Order order = new Order();
        order.setUserId(request.getUserId());
        order.setTotalAmount(totalAmount);
        order.setPaymentMethod(Order.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase()));
        order.setShippingAddress(request.getShippingAddress());
        order.setStatus(Order.OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductId(itemRequest.getProductId());
            orderItem.setProductName(itemRequest.getProductName());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(itemRequest.getPrice());
            orderItem.setSubtotal(
                itemRequest.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()))
            );
            orderItems.add(orderItem);
        }
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        // Step 4: Process payment
        Map<String, Object> paymentRequest = new HashMap<>();
        paymentRequest.put("orderId", savedOrder.getId());
        paymentRequest.put("userId", request.getUserId());
        paymentRequest.put("amount", totalAmount);
        paymentRequest.put("paymentMethod", request.getPaymentMethod());

        Map<String, Object> paymentResponse = paymentClient.processPayment(paymentRequest);
        String paymentStatus = (String) paymentResponse.get("status");

        if ("SUCCESS".equals(paymentStatus)) {
            // Step 5a: Payment success — reduce stock and confirm order
            for (OrderItemRequest item : request.getItems()) {
                inventoryClient.reduceStock(item.getProductId(), item.getQuantity());
            }
            savedOrder.setStatus(Order.OrderStatus.CONFIRMED);
            Object paymentId = paymentResponse.get("id");
            if (paymentId != null) {
                savedOrder.setPaymentId(Long.valueOf(paymentId.toString()));
            }
        } else {
            // Step 5b: Payment failed — mark order accordingly
            savedOrder.setStatus(Order.OrderStatus.PAYMENT_FAILED);
        }

        return OrderResponse.from(orderRepository.save(savedOrder));
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return OrderResponse.from(order);
    }

    public List<OrderResponse> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId)
                .stream()
                .map(OrderResponse::from)
                .collect(Collectors.toList());
    }

    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (order.getStatus() == Order.OrderStatus.SHIPPED ||
            order.getStatus() == Order.OrderStatus.DELIVERED) {
            throw new RuntimeException("Cannot cancel order that is already shipped or delivered");
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        return OrderResponse.from(orderRepository.save(order));
    }
}
