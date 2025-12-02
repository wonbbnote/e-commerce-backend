package kr.hhplus.be.server.order.infrastructure.mapper;

import kr.hhplus.be.server.order.domain.model.Payment;
import kr.hhplus.be.server.order.infrastructure.entity.PaymentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final OrderMapper orderMapper;

    /**
     * Payment (도메인) → PaymentEntity (JPA 엔티티)
     */
    public PaymentEntity toEntity(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentEntity.builder()
                .id(payment.getId())
                .order(payment.getOrder() != null ? orderMapper.toEntity(payment.getOrder()) : null)
                .status(payment.getStatus())
                .paymentAmount(payment.getPaymentAmount())
                .paidAt(payment.getPaidAt())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    /**
     * PaymentEntity (JPA 엔티티) → Payment (도메인)
     * Mapper용 생성자를 사용하여 객체 생성
     */
    public Payment toDomain(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        // OrderEntity를 Order로 변환
        var order = entity.getOrder() != null ? orderMapper.toDomain(entity.getOrder()) : null;

        // Mapper용 생성자로 Payment 객체 생성
        return new Payment(
                entity.getId(),
                order,
                entity.getStatus(),
                entity.getPaymentAmount(),
                entity.getPaidAt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
