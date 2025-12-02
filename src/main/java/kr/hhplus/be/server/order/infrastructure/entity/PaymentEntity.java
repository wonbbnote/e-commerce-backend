package kr.hhplus.be.server.order.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.PaymentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order")
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    private Integer paymentAmount;  // 결제 금액
    private LocalDateTime paidAt;   // 결제 완료 시각

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
