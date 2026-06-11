package com.ecommerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;

/**
 * Feign client for Inventory Service.
 * Uses service name from Eureka — no hardcoded URL needed.
 */
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/inventory/check/{productId}")
    Map<String, Object> checkStock(
        @PathVariable("productId") Long productId,
        @RequestParam("quantity") Integer quantity
    );

    @PutMapping("/inventory/reduce/{productId}")
    Map<String, Object> reduceStock(
        @PathVariable("productId") Long productId,
        @RequestParam("quantity") Integer quantity
    );
}
