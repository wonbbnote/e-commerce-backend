package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

    private Long id;
    private Order order;
    private Product product;

    private Integer quantity;
    private Integer unitPrice;      // 주문 시점의 상품 가격

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 주문항목 생성용 생성자
    public OrderItem(Order order, Product product, Integer quantity) {
        this.order = order;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = product.getPrice();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Mapper용 생성자 (DB에서 조회한 데이터로 객체 재구성)
    public OrderItem(Long id, Product product, Integer quantity, Integer unitPrice,
                     LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

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
