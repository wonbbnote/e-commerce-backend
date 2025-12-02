package kr.hhplus.be.server.order.domain.model;

import jakarta.persistence.*;
import jdk.jshell.Snippet;
import kr.hhplus.be.server.user.domain.User;
import lombok.Builder;
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
        this.status = OrderStatus.CONFIRMED;
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
     * 주문 항목 제거
     */
    public void removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    /**
     * 주문 확인
     */
    public void confirm() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Only pending orders can be confirmed");
        }
        this.status = OrderStatus.CONFIRMED;
    }

    /**
     * 주문 취소 (배송 전까지만 가능)
     */
    public void cancel() {
        if (this.status == OrderStatus.SHIPPED || this.status == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot cancel shipped or delivered orders");
        }
        this.status = OrderStatus.CANCELLED;
    }

    /**
     * 주문이 취소 가능한지 확인
     */
    public boolean isCancellable() {
        return status != OrderStatus.SHIPPED && status != OrderStatus.DELIVERED && status != OrderStatus.CANCELLED;
    }
}
