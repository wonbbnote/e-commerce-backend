package kr.hhplus.be.server.order.infrastructure.persistence;

import kr.hhplus.be.server.order.domain.model.Payment;
import kr.hhplus.be.server.order.domain.repository.PaymentRepository;
import kr.hhplus.be.server.order.infrastructure.entity.PaymentEntity;
import kr.hhplus.be.server.order.infrastructure.jpa.JpaPaymentRepository;
import kr.hhplus.be.server.order.infrastructure.mapper.PaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final PaymentMapper paymentMapper;

    /**
     * Payment (도메인) → PaymentEntity (엔티티) → DB 저장 → PaymentEntity → Payment (도메인) 반환
     */
    @Override
    @Transactional
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentMapper.toEntity(payment);
        PaymentEntity saved = jpaPaymentRepository.save(entity);
        return paymentMapper.toDomain(saved);
    }

    /**
     * PaymentEntity (엔티티) → Payment (도메인) 변환하여 반환
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(Long id) {
        return jpaPaymentRepository.findById(id)
                .map(paymentMapper::toDomain);
    }
}
