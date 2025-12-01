package kr.hhplus.be.server.coupon.dto;

public record CouponCreateRequest(String couponName, Integer discountAmount, Integer totalQuantity, String expiredAt) {
}
