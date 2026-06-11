package com.ecommerce.payment.dto;

import com.ecommerce.payment.model.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private String transactionId;
    private String failureReason;
    private LocalDateTime createdAt;

    public static PaymentResponse from(Payment payment) {
        PaymentResponse response = new PaymentResponse();
        response.id            = payment.getId();
        response.orderId       = payment.getOrderId();
        response.userId        = payment.getUserId();
        response.amount        = payment.getAmount();
        response.paymentMethod = payment.getPaymentMethod().name();
        response.status        = payment.getStatus().name();
        response.transactionId = payment.getTransactionId();
        response.failureReason = payment.getFailureReason();
        response.createdAt     = payment.getCreatedAt();
        return response;
    }

    // Getters
    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public Long getUserId() { return userId; }
    public BigDecimal getAmount() { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public String getTransactionId() { return transactionId; }
    public String getFailureReason() { return failureReason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
