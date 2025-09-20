package kr.hhplus.be.server.coupon.dto;

import java.time.LocalDateTime;

public record CouponIssueResponseDto(
        String couponName,
        Integer discountAmount,
        LocalDateTime expiresAt
) {
}
