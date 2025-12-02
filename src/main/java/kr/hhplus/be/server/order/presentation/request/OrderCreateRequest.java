package kr.hhplus.be.server.order.presentation.request;

import java.util.List;

public record OrderCreateRequest(Long userId, List<OrderItemRequest> items, Long userCouponId) {
}
