package kr.hhplus.be.server.order.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDto(
    Long orderId,
    Long userId,
    List<OrderItemResponse> items,
    Integer totalAmount,
    LocalDateTime orderedAt
) {
    public record OrderItemResponse(
        Long productId,
        String productName,
        Integer price,
        Integer quantity
    ) {}
}