package kr.hhplus.be.server.coupon.dto;

import jakarta.validation.constraints.NotNull;

public record CouponIssueRequest(
        @NotNull Long couponId) {
}
