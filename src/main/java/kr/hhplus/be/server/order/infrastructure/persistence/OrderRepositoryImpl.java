package kr.hhplus.be.server.order.infrastructure.persistence;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.infrastructure.entity.OrderEntity;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaOrderRepository;
import kr.hhplus.be.server.order.infrastructure.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final OrderMapper orderMapper;

    /**
     * Order (도메인) → OrderEntity (엔티티) → DB 저장 → OrderEntity → Order (도메인) 반환
     */
    @Override
    @Transactional
    public Order save(Order order) {
        // 도메인 → 엔티티
        OrderEntity entity = orderMapper.toEntity(order);

        // DB 저장
        OrderEntity saved = jpaOrderRepository.save(entity);

        // 엔티티 → 도메인 (ID 할당됨)
        return orderMapper.toDomain(saved);
    }

    /**
     * OrderEntity (엔티티) → Order (도메인) 변환하여 반환
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return jpaOrderRepository.findById(id)
                .map(orderMapper::toDomain);
    }
}
