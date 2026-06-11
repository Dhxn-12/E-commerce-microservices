package com.ecommerce.order.dto;

import com.ecommerce.order.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderResponse {

    private Long id;
    private Long userId;
    private List<OrderItemResponse> items;
    private BigDecimal totalAmount;
    private String status;
    private String paymentMethod;
    private Long paymentId;
    private String shippingAddress;
    private LocalDateTime createdAt;

    public static OrderResponse from(Order order) {
        OrderResponse response = new OrderResponse();
        response.id              = order.getId();
        response.userId          = order.getUserId();
        response.totalAmount     = order.getTotalAmount();
        response.status          = order.getStatus().name();
        response.paymentMethod   = order.getPaymentMethod().name();
        response.paymentId       = order.getPaymentId();
        response.shippingAddress = order.getShippingAddress();
        response.createdAt       = order.getCreatedAt();
        if (order.getItems() != null) {
            response.items = order.getItems()
                    .stream()
                    .map(OrderItemResponse::from)
                    .collect(Collectors.toList());
        }
        return response;
    }

    // Getters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public List<OrderItemResponse> getItems() { return items; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getStatus() { return status; }
    public String getPaymentMethod() { return paymentMethod; }
    public Long getPaymentId() { return paymentId; }
    public String getShippingAddress() { return shippingAddress; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
