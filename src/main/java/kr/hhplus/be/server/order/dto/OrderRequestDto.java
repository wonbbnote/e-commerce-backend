package kr.hhplus.be.server.order.dto;

import java.util.List;

public record OrderRequestDto(
    Long userId,
    List<OrderItem> items,
    Long couponId
) {
    public record OrderItem(Long productId, Integer quantity) {}
}