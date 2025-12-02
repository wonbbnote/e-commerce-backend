package kr.hhplus.be.server.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.exception.BusinessException;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Integer price;
    private Integer stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Product(String productName, Integer price, Integer stock) {
        // 입력값 검증
        validateProductInput(productName, price, stock);
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }


    /**
     * 상품 재고를 차감한다.
     * @param quantity 구매하는 상품의 개수
     */
    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new BusinessException.InvalidQuantityException();
        }
        if(quantity > this.stock){
            throw new BusinessException.ProductOutOfStockException(this.id);
        }
        this.stock -= quantity;
        this.updatedAt = LocalDateTime.now();
    }


    /**
     * 상품 입력값 검증
     */
    private void validateProductInput(String productName, Integer price, Integer stock) {
        if (productName == null || productName.isBlank()) {
            throw new BusinessException.MissingProductNameException();
        }
        if (productName.length() > 255) {
            throw new BusinessException.InvalidateProductNameLengthException();
        }
        if (price == null || price <= 0) {
            throw new BusinessException.InvalidatePriceException();
        }
        if (stock == null || stock < 0) {
            throw new BusinessException.InvalidateStockException();
        }
    }


}
