package kr.hhplus.be.server.order.dto;

import java.time.LocalDateTime;

public record PaymentResponseDto(
    Long paymentId,
    Long orderId,
    String status,
    Integer amount,
    LocalDateTime paidAt
) {
}