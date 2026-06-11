package com.ecommerce.order.dto;

import com.ecommerce.order.model.OrderItem;
import java.math.BigDecimal;

public class OrderItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;

    public static OrderItemResponse from(OrderItem item) {
        OrderItemResponse response = new OrderItemResponse();
        response.id          = item.getId();
        response.productId   = item.getProductId();
        response.productName = item.getProductName();
        response.quantity    = item.getQuantity();
        response.price       = item.getPrice();
        response.subtotal    = item.getSubtotal();
        return response;
    }

    // Getters
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public BigDecimal getSubtotal() { return subtotal; }
}
