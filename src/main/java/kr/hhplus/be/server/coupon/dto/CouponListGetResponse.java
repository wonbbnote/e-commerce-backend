package kr.hhplus.be.server.coupon.dto;

import kr.hhplus.be.server.coupon.domain.Coupon;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CouponListGetResponse(Long id, String couponName, Integer discountAmount,
                                    Integer totalQuantity, LocalDateTime expiredAt) {

    public static CouponListGetResponse from(Coupon coupon) {
        return CouponListGetResponse.builder()
                .id(coupon.getId())
                .couponName(coupon.getCouponName())
                .discountAmount(coupon.getDiscountAmount())
                .totalQuantity(coupon.getTotalQuantity())
                .expiredAt(coupon.getExpiredAt())
                .build();
    }
}
