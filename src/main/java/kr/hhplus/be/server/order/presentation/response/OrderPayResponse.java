package kr.hhplus.be.server.order.presentation.response;

import kr.hhplus.be.server.order.domain.model.Payment;
import lombok.Builder;

import java.time.LocalDateTime;

public record OrderPayResponse(Long paymentId, Long orderId, String status, Integer paymentAmount,
                               LocalDateTime paidAt, LocalDateTime createdAt) {
}
