package kr.hhplus.be.server.order.infrastructure.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.order.domain.model.OrderStatus;
import kr.hhplus.be.server.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "orderItems")
@EqualsAndHashCode(exclude = "orderItems")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItems;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private Integer totalAmount;
    private Integer discountAmount;
    private Integer finalAmount;
    private LocalDateTime orderedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
