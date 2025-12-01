package kr.hhplus.be.server.coupon.dto;

import kr.hhplus.be.server.coupon.domain.UserCoupon;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserCouponResponse(Long userCouponId, Boolean isUsed, LocalDateTime usedAt, LocalDateTime issuedAt, LocalDateTime updatedAt,
                                 Long couponId, String couponName, Integer discountAmount, Integer totalQuantity, Integer remainingQuantity,
                                 LocalDateTime expiredAt, LocalDateTime couponCreatedAt) {
    public static UserCouponResponse from(UserCoupon userCoupon) {

        return UserCouponResponse.builder().userCouponId(userCoupon.getId())
            .isUsed(userCoupon.getIsUsed())
            .usedAt(userCoupon.getUsedAt())
            .issuedAt(userCoupon.getCreatedAt())
            .updatedAt(userCoupon.getUpdatedAt())
            .couponId(userCoupon.getCoupon().getId())
            .couponName(userCoupon.getCoupon().getCouponName())
            .discountAmount(userCoupon.getCoupon().getDiscountAmount())
            .totalQuantity(userCoupon.getCoupon().getTotalQuantity())
            .remainingQuantity(userCoupon.getCoupon().getRemainingQuantity())
            .expiredAt(userCoupon.getCoupon().getExpiredAt())
            .couponCreatedAt(userCoupon.getCoupon().getCreatedAt())
            .build();
        }
    }
