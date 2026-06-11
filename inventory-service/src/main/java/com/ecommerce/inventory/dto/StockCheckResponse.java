package com.ecommerce.inventory.dto;

public class StockCheckResponse {

    private Long productId;
    private boolean available;
    private Integer availableQuantity;
    private String message;

    public StockCheckResponse(Long productId, boolean available, Integer availableQuantity, String message) {
        this.productId         = productId;
        this.available         = available;
        this.availableQuantity = availableQuantity;
        this.message           = message;
    }

    // Getters
    public Long getProductId() { return productId; }
    public boolean isAvailable() { return available; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public String getMessage() { return message; }
}
