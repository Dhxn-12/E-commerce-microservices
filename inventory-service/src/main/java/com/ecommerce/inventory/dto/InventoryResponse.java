package com.ecommerce.inventory.dto;

import com.ecommerce.inventory.model.Inventory;
import java.time.LocalDateTime;

public class InventoryResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private String status;
    private LocalDateTime updatedAt;

    public static InventoryResponse from(Inventory inventory) {
        InventoryResponse response = new InventoryResponse();
        response.id                = inventory.getId();
        response.productId         = inventory.getProductId();
        response.productName       = inventory.getProductName();
        response.quantity          = inventory.getQuantity();
        response.reservedQuantity  = inventory.getReservedQuantity();
        response.availableQuantity = inventory.getQuantity() - inventory.getReservedQuantity();
        response.status            = inventory.getStatus().name();
        response.updatedAt         = inventory.getUpdatedAt();
        return response;
    }

    // Getters
    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getProductName() { return productName; }
    public Integer getQuantity() { return quantity; }
    public Integer getReservedQuantity() { return reservedQuantity; }
    public Integer getAvailableQuantity() { return availableQuantity; }
    public String getStatus() { return status; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
