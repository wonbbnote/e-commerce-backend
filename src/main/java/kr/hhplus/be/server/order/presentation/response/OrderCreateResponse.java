package kr.hhplus.be.server.order.presentation.response;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderCreateResponse(Long orderId, Long userId, List<OrderItemResponse> orderItems, Integer totalAmount,
                                  Integer discountAmount, Integer finalAmount, OrderStatus status, LocalDateTime orderedAt, LocalDateTime createdAt) {


    public static OrderCreateResponse from(Order order) {
        List<OrderItemResponse> items = order.getOrderItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getProductName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

        return OrderCreateResponse.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .orderItems(items)
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .status(order.getStatus())
                .orderedAt(order.getOrderedAt())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
