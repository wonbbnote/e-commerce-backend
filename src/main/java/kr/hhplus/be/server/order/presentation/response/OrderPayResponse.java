package kr.hhplus.be.server.order.presentation.response;

import kr.hhplus.be.server.order.domain.model.Payment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OrderPayResponse(Long paymentId, Long orderId, String status, Integer paymentAmount,
                               LocalDateTime paidAt, LocalDateTime createdAt) {

    /**
     * Payment (도메인) → OrderPayResponse (DTO)
     */
    public static OrderPayResponse from(Payment payment) {
        return OrderPayResponse.builder()
                .paymentId(payment.getId())
                .orderId(payment.getOrder().getId())
                .status(payment.getStatus().toString())
                .paymentAmount(payment.getPaymentAmount())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
