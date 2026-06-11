package com.ecommerce.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.Map;

/**
 * Feign client for Payment Service.
 * Uses service name from Eureka — no hardcoded URL needed.
 */
@FeignClient(name = "payment-service")
public interface PaymentClient {

    @PostMapping("/payments")
    Map<String, Object> processPayment(@RequestBody Map<String, Object> paymentRequest);
}
