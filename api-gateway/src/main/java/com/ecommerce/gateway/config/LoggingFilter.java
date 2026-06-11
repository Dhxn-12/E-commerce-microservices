package com.ecommerce.gateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import reactor.core.publisher.Mono;

/**
 * Global logging filter — logs every request that passes through the gateway.
 * Useful for debugging routing issues during development.
 *
 * Example output:
 *   REQUEST  → GET  /api/users/1
 *   RESPONSE → GET  /api/users/1  [200]
 */
@Configuration
public class LoggingFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Bean
    @Order(1)
    public GlobalFilter requestLoggingFilter() {
        return (exchange, chain) -> {
            String method = exchange.getRequest().getMethod().name();
            String path   = exchange.getRequest().getURI().getPath();

            log.info("REQUEST  → {} {}", method, path);

            return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                int status = exchange.getResponse().getStatusCode() != null
                        ? exchange.getResponse().getStatusCode().value()
                        : 0;
                log.info("RESPONSE → {} {} [{}]", method, path, status);
            }));
        };
    }
}
