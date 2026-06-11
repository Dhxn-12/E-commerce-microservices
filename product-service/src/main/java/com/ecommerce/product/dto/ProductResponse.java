package com.ecommerce.product.dto;

import com.ecommerce.product.model.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String brand;
    private String imageUrl;
    private boolean active;
    private LocalDateTime createdAt;

    public static ProductResponse from(Product product) {
        ProductResponse response = new ProductResponse();
        response.id          = product.getId();
        response.name        = product.getName();
        response.description = product.getDescription();
        response.price       = product.getPrice();
        response.category    = product.getCategory();
        response.brand       = product.getBrand();
        response.imageUrl    = product.getImageUrl();
        response.active      = product.isActive();
        response.createdAt   = product.getCreatedAt();
        return response;
    }

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getCategory() { return category; }
    public String getBrand() { return brand; }
    public String getImageUrl() { return imageUrl; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
