package com.ecommerce.payment.service;

import com.ecommerce.payment.dto.PaymentRequest;
import com.ecommerce.payment.dto.PaymentResponse;
import com.ecommerce.payment.model.Payment;
import com.ecommerce.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponse processPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(
            Payment.PaymentMethod.valueOf(request.getPaymentMethod().toUpperCase())
        );

        // Simulate payment processing
        // In production: integrate with Razorpay, Stripe, PayU etc.
        boolean paymentSuccess = simulatePaymentGateway(request);

        if (paymentSuccess) {
            payment.setStatus(Payment.PaymentStatus.SUCCESS);
            payment.setTransactionId("TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        } else {
            payment.setStatus(Payment.PaymentStatus.FAILED);
            payment.setFailureReason("Payment declined by gateway");
        }

        Payment saved = paymentRepository.save(payment);
        return PaymentResponse.from(saved);
    }

    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
    }

    public List<PaymentResponse> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId)
                .stream()
                .map(PaymentResponse::from)
                .collect(Collectors.toList());
    }

    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return PaymentResponse.from(payment);
    }

    public PaymentResponse refundPayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        if (payment.getStatus() != Payment.PaymentStatus.SUCCESS) {
            throw new RuntimeException("Only successful payments can be refunded");
        }

        payment.setStatus(Payment.PaymentStatus.REFUNDED);
        return PaymentResponse.from(paymentRepository.save(payment));
    }

    // Simulates a payment gateway — always succeeds in dev
    // Replace with real gateway (Razorpay/Stripe) in production
    private boolean simulatePaymentGateway(PaymentRequest request) {
        // COD always succeeds
        if ("COD".equalsIgnoreCase(request.getPaymentMethod())) {
            return true;
        }
        // Simulate 90% success rate for other methods
        return Math.random() > 0.1;
    }
}
