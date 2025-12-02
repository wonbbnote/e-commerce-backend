package kr.hhplus.be.server.order.infrastructure.mapper;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.infrastructure.entity.OrderEntity;
import kr.hhplus.be.server.order.infrastructure.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderMapper {

    private final OrderItemMapper orderItemMapper;

    /**
     * Order (도메인) → OrderEntity (JPA 엔티티)
     */
    public OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }

        List<OrderItemEntity> itemEntities = new ArrayList<>();

        OrderEntity entity = OrderEntity.builder()
                .id(order.getId())
                .user(order.getUser())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .discountAmount(order.getDiscountAmount())
                .finalAmount(order.getFinalAmount())
                .orderedAt(order.getOrderedAt())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItems(itemEntities)
                .build();

        // OrderItem을 OrderItemEntity로 변환하고 order 설정
        if (order.getOrderItems() != null) {
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemEntity itemEntity = orderItemMapper.toEntity(orderItem);
                itemEntity.setOrder(entity);
                itemEntities.add(itemEntity);
            }
        }

        return entity;
    }

    /**
     * OrderEntity (JPA 엔티티) → Order (도메인)
     * Mapper용 생성자를 사용하여 객체 생성
     */
    public Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        // OrderItemEntity를 OrderItem으로 변환
        List<OrderItem> orderItems = new ArrayList<>();
        if (entity.getOrderItems() != null) {
            orderItems = entity.getOrderItems().stream()
                    .map(orderItemMapper::toDomain)
                    .collect(Collectors.toList());
        }

        // Mapper용 생성자로 Order 객체 생성
        Order order = new Order(
                entity.getId(),
                entity.getUser(),
                orderItems,
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getDiscountAmount(),
                entity.getFinalAmount(),
                entity.getOrderedAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );

        // OrderItem에 Order 설정
        orderItems.forEach(item -> item.setOrder(order));

        return order;
    }
}
