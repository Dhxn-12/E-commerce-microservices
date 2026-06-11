package com.ecommerce.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * Order Service — the heart of the e-commerce system.
 * 
 * When a order is placed it:
 * 1. Checks stock with Inventory Service (Feign)
 * 2. Processes payment with Payment Service (Feign)
 * 3. Reduces stock in Inventory Service (Feign)
 * 4. Saves the order in its own database
 *
 * Accessible via API Gateway at: localhost:8080/api/orders/
 */
@SpringBootApplication
@EnableFeignClients
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}
