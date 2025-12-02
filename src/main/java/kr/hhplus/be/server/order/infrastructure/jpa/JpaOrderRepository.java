package kr.hhplus.be.server.order.infrastructure.jpa;

import kr.hhplus.be.server.order.infrastructure.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findById(Long id);
}
