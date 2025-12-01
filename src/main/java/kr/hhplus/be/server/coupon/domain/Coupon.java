package kr.hhplus.be.server.coupon.domain;

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
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String couponName;
    private Integer discountAmount;
    private Integer totalQuantity;
    private Integer remainingQuantity;
    private LocalDateTime expiredAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 쿠폰이 만료되었는지 확인
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }

    /**
     * 쿠폰을 발급 가능한지 확인
     */
    public boolean isAvailable() {
        return !isExpired() && remainingQuantity > 0;
    }

    /**
     * 쿠폰 재고 감소
     * @throws IllegalArgumentException 재고가 부족할 때
     */
    public void issue() {
        if (remainingQuantity <= 0) {
            throw new IllegalArgumentException("Coupon stock is exhausted");
        }
        this.remainingQuantity--;
    }

}
