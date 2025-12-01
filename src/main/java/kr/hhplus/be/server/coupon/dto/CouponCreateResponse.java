package kr.hhplus.be.server.coupon.dto;

import kr.hhplus.be.server.coupon.domain.Coupon;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CouponCreateResponse(Long id, String couponName, Integer discountAmount, Integer totalQuantity,
                                   LocalDateTime expiredAt, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static CouponCreateResponse from(Coupon coupon) {
        return CouponCreateResponse.builder()
                .id(coupon.getId())
                .couponName(coupon.getCouponName())
                .discountAmount(coupon.getDiscountAmount())
                .totalQuantity(coupon.getTotalQuantity())
                .expiredAt(coupon.getExpiredAt())
                .createdAt(coupon.getCreatedAt())
                .updatedAt(coupon.getUpdatedAt())
                .build();
    }
}
