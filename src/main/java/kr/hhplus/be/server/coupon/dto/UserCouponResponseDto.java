package kr.hhplus.be.server.coupon.dto;

import java.time.LocalDateTime;

public record UserCouponResponseDto(

    Long couponId,
    String couponName,
    Integer discountAmount,
    Boolean isUsed,
    LocalDateTime expiresAt
) {
}