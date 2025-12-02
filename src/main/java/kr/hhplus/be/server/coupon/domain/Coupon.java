package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.exception.BusinessException;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public Coupon(String couponName, Integer discountAmount, Integer totalQuantity, LocalDateTime expiredAt) {
        validateCouponInput(couponName, discountAmount, totalQuantity, expiredAt);
        this.couponName = couponName;
        this.discountAmount = discountAmount;
        this.totalQuantity = totalQuantity;
        this.remainingQuantity = totalQuantity;
        this.expiredAt = expiredAt;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

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
     * @throws BusinessException.CouponOutOfStockException 재고가 부족할 때
     */
    public void issue() {
        if (remainingQuantity <= 0) {
            throw new BusinessException.CouponOutOfStockException();
        }
        this.remainingQuantity--;
    }


    /**
     * 입력값 검증
     */
    private void validateCouponInput(String couponName, Integer discountAmount,
                                     Integer totalQuantity, LocalDateTime expiredAt){

        // couponName
        if(couponName == null || couponName.isBlank()){
            throw new BusinessException.MissingCouponNameException();
        }

        if (couponName.length() > 255) {
            throw new BusinessException.InvalidateCouponNameLengthException();
        }

        // discountAmount
        if (discountAmount == null || discountAmount <= 0) {
            throw new BusinessException.InvalidateDiscountAmountException();
        }

        // totalQuantity
        if (totalQuantity == null || totalQuantity <= 0) {
            throw new BusinessException.InvalidateQuantityException();
        }

        // expiredAt가 현재 시간보다 이후인지 검증
        LocalDateTime now = LocalDateTime.now();
        if (expiredAt.isBefore(now) || expiredAt.equals(now)) {
            throw new BusinessException.AlreadyExpiredException();
        }
    }


}
