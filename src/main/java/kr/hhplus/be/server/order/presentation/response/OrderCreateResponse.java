package kr.hhplus.be.server.order.presentation.response;

import java.time.LocalDateTime;
import java.util.List;

public record OrderCreateResponse(Long orderId, Long userId, List<OrderItemResponse>orderItems, Integer totalAmount,
        Integer discountAmount, Integer finalAmount, String status, LocalDateTime orderedAt, LocalDateTime createdAt) {


}
