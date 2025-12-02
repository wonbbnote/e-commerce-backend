package kr.hhplus.be.server.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record CouponCreateRequest(
        @NotBlank String couponName,
        @NotNull @Positive Integer discountAmount,
        @NotNull @PositiveOrZero Integer totalQuantity,
        @NotBlank String expiredAt) {
}
