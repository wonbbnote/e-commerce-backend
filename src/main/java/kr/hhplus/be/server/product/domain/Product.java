package kr.hhplus.be.server.product.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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


    /**
     * 상품 재고를 차감한다.
     * @param count 구매한 상품의 개수
     */
    public void stockDown(Integer count) {
        if (count == null || count <= 0) {
            throw new IllegalArgumentException("0 이상");
        }
        this.stock -= count;
        this.updatedAt = LocalDateTime.now();
    }


}
