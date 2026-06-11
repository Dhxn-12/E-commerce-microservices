package com.ecommerce.inventory.controller;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.StockCheckResponse;
import com.ecommerce.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // GET http://localhost:8080/api/inventory
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }

    // GET http://localhost:8080/api/inventory/product/1
    @GetMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> getByProductId(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getByProductId(productId));
    }

    // GET http://localhost:8080/api/inventory/check/1?quantity=5
    @GetMapping("/check/{productId}")
    public ResponseEntity<StockCheckResponse> checkStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.checkStock(productId, quantity));
    }

    // POST http://localhost:8080/api/inventory
    @PostMapping
    public ResponseEntity<InventoryResponse> addInventory(@Valid @RequestBody InventoryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inventoryService.addInventory(request));
    }

    // PUT http://localhost:8080/api/inventory/product/1?quantity=100
    @PutMapping("/product/{productId}")
    public ResponseEntity<InventoryResponse> updateStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.updateStock(productId, quantity));
    }

    // PUT http://localhost:8080/api/inventory/reduce/1?quantity=2
    @PutMapping("/reduce/{productId}")
    public ResponseEntity<InventoryResponse> reduceStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.reduceStock(productId, quantity));
    }
}
