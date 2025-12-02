package kr.hhplus.be.server.order.presentation.response;

public record OrderItemResponse(Long orderItemId, Long productId, String productName, Integer quantity, Integer unitPrice) {
}
