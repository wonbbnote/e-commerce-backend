package kr.hhplus.be.server.order.domain.model;

import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import kr.hhplus.be.server.product.domain.Product;

import java.time.LocalDateTime;

public class OrderItem {

    private Long id;
    private Order order;
    private Product product;

    private Integer quantity;
    private Integer unitPrice;      // 주문 시점의 상품 가격

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    /**
     * Order 설정
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 최종 가격 계산
     * @return unitPrice * quantity
     */
    public Integer calculateFinalPrice() {
        int itemTotal = this.unitPrice * this.quantity;
        return itemTotal;
    }


}
