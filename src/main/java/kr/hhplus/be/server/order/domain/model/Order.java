package kr.hhplus.be.server.order.domain.model;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {

    private Long id;
    private User user;
    private List<OrderItem> orderItems = new ArrayList<>();
    private OrderStatus status;

    private Integer totalAmount;
    private Integer discountAmount;
    private Integer finalAmount;
    private LocalDateTime orderedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
