package com.ecommerce.inventory.service;

import com.ecommerce.inventory.dto.InventoryRequest;
import com.ecommerce.inventory.dto.InventoryResponse;
import com.ecommerce.inventory.dto.StockCheckResponse;
import com.ecommerce.inventory.model.Inventory;
import com.ecommerce.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public List<InventoryResponse> getAllInventory() {
        return inventoryRepository.findAll()
                .stream()
                .map(InventoryResponse::from)
                .collect(Collectors.toList());
    }

    public InventoryResponse getByProductId(Long productId) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));
        return InventoryResponse.from(inventory);
    }

    // Called by Order Service to check if stock is available
    public StockCheckResponse checkStock(Long productId, Integer requiredQuantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElse(null);

        if (inventory == null) {
            return new StockCheckResponse(productId, false, 0, "Product not found in inventory");
        }

        int available = inventory.getQuantity() - inventory.getReservedQuantity();

        if (available >= requiredQuantity) {
            return new StockCheckResponse(productId, true, available, "Stock available");
        } else {
            return new StockCheckResponse(productId, false, available,
                    "Insufficient stock. Available: " + available + ", Required: " + requiredQuantity);
        }
    }

    public InventoryResponse addInventory(InventoryRequest request) {
        if (inventoryRepository.existsByProductId(request.getProductId())) {
            throw new RuntimeException("Inventory already exists for product: " + request.getProductId());
        }

        Inventory inventory = new Inventory();
        inventory.setProductId(request.getProductId());
        inventory.setProductName(request.getProductName());
        inventory.setQuantity(request.getQuantity());

        return InventoryResponse.from(inventoryRepository.save(inventory));
    }

    @Transactional
    public InventoryResponse updateStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        inventory.setQuantity(quantity);
        return InventoryResponse.from(inventoryRepository.save(inventory));
    }

    // Called by Order Service to reduce stock when order is placed
    @Transactional
    public InventoryResponse reduceStock(Long productId, Integer quantity) {
        Inventory inventory = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found for product: " + productId));

        int available = inventory.getQuantity() - inventory.getReservedQuantity();
        if (available < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }

        inventory.setQuantity(inventory.getQuantity() - quantity);
        return InventoryResponse.from(inventoryRepository.save(inventory));
    }
}
