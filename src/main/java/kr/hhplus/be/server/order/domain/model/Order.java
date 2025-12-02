package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.user.domain.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class Order {

    private Long id;
    private User user;
    private List<OrderItem> orderItems;
    private OrderStatus status;

    private Integer totalAmount;
    private Integer discountAmount;
    private Integer finalAmount;
    private LocalDateTime orderedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 주문 생성용 생성자
    public Order(User user, Integer totalAmount, Integer discountAmount) {
        this.user = user;
        this.orderItems = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = totalAmount - discountAmount;
        this.orderedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Mapper용 생성자 (DB에서 조회한 데이터로 객체 재구성)
    public Order(Long id, User user, List<OrderItem> orderItems, OrderStatus status,
                 Integer totalAmount, Integer discountAmount, Integer finalAmount,
                 LocalDateTime orderedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.user = user;
        this.orderItems = orderItems != null ? orderItems : new ArrayList<>();
        this.status = status;
        this.totalAmount = totalAmount;
        this.discountAmount = discountAmount;
        this.finalAmount = finalAmount;
        this.orderedAt = orderedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    /**
     * 주문 항목 추가
     */
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * 결제 완료 시 상태 변경
     */
    public void paidSuccess() {
        if (this.status != OrderStatus.PENDING) {
            throw new BusinessException.InvalidOrderStatusException();
        }
        this.status = OrderStatus.PAID;
    }
}
