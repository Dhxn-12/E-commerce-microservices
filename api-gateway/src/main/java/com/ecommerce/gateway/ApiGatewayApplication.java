package com.ecommerce.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * API Gateway — single entry point for all client requests.
 *
 * Routes incoming requests to the correct microservice
 * using service names registered in Eureka (no hardcoded URLs).
 *
 * Running on: http://localhost:8080
 */
@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
