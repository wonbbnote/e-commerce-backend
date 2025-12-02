package kr.hhplus.be.server.order.infrastructure.mapper;

import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.infrastructure.entity.OrderItemEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {

    /**
     * OrderItem (도메인) → OrderItemEntity (JPA 엔티티)
     */
    public OrderItemEntity toEntity(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }

        return OrderItemEntity.builder()
                .id(orderItem.getId())
                .product(orderItem.getProduct())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .createdAt(orderItem.getCreatedAt())
                .updatedAt(orderItem.getUpdatedAt())
                .build();
    }

    /**
     * OrderItemEntity (JPA 엔티티) → OrderItem (도메인)
     * Mapper용 생성자를 사용하여 객체 생성
     */
    public OrderItem toDomain(OrderItemEntity entity) {
        if (entity == null) {
            return null;
        }

        return new OrderItem(
                entity.getId(),
                entity.getProduct(),
                entity.getQuantity(),
                entity.getUnitPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
