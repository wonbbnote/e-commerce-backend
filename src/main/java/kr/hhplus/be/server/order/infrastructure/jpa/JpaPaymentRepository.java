package kr.hhplus.be.server.order.infrastructure.jpa;

import kr.hhplus.be.server.order.domain.model.Payment;
import kr.hhplus.be.server.order.infrastructure.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPaymentRepository extends JpaRepository<PaymentEntity, Long> {
}
