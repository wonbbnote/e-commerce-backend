package kr.hhplus.be.server.common.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    public BusinessException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    // User 관련 예외
    public static class InvalidEmailException extends BusinessException {
        public InvalidEmailException() {
            super("INVALID_EMAIL", "이메일은 필수입니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidPasswordException extends BusinessException {
        public InvalidPasswordException() {
            super("INVALID_PASSWORD", "비밀번호는 필수입니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class DuplicateEmailException extends BusinessException {
        public DuplicateEmailException(String email) {
            super("DUPLICATE_EMAIL", "이미 존재하는 이메일입니다: " + email, HttpStatus.CONFLICT);
        }
    }

    public static class UserNotFoundException extends BusinessException {
        public UserNotFoundException(Long id) {
            super("USER_NOT_FOUND", "사용자를 찾을 수 없습니다: " + id, HttpStatus.NOT_FOUND);
        }
    }

    // Balance 관련 예외
    public static class BalanceNotFoundException extends BusinessException {
        public BalanceNotFoundException(Long userId) {
            super("BALANCE_NOT_FOUND", "잔액 정보를 찾을 수 없습니다: " + userId, HttpStatus.NOT_FOUND);
        }
    }

    public static class InsufficientBalanceException extends BusinessException {
        public InsufficientBalanceException(Long userId) {
            super("INSUFFICIENT_BALANCE", "잔액이 부족합니다", HttpStatus.BAD_REQUEST);
        }
    }

    // Coupon 관련 예외
    public static class CouponNotFoundException extends BusinessException {
        public CouponNotFoundException(Long couponId) {
            super("COUPON_NOT_FOUND", "쿠폰을 찾을 수 없습니다: " + couponId, HttpStatus.NOT_FOUND);
        }
    }

    public static class CouponOutOfStockException extends BusinessException {
        public CouponOutOfStockException(Long couponId) {
            super("COUPON_OUT_OF_STOCK", "쿠폰 재고가 부족합니다: " + couponId, HttpStatus.BAD_REQUEST);
        }
    }

    public static class CouponExpiredException extends BusinessException {
        public CouponExpiredException(Long couponId) {
            super("COUPON_EXPIRED", "만료된 쿠폰입니다: " + couponId, HttpStatus.BAD_REQUEST);
        }
    }

    public static class CouponAlreadyIssuedException extends BusinessException {
        public CouponAlreadyIssuedException(Long userId, Long couponId) {
            super("COUPON_ALREADY_ISSUED", "이미 발급받은 쿠폰입니다: " + userId + ", " + couponId, HttpStatus.CONFLICT);
        }
    }

    public static class CouponAlreadyUsedException extends BusinessException {
        public CouponAlreadyUsedException(Long couponId) {
            super("COUPON_ALREADY_USED", "이미 사용된 쿠폰입니다: " + couponId, HttpStatus.CONFLICT);
        }
    }

    public static class UserCouponNotFoundException extends BusinessException {
        public UserCouponNotFoundException(Long userId, Long couponId) {
            super("USER_COUPON_NOT_FOUND", "사용자의 쿠폰을 찾을 수 없습니다: " + userId + ", " + couponId, HttpStatus.NOT_FOUND);
        }

        public UserCouponNotFoundException(Long userCouponId) {
            super("USER_COUPON_NOT_FOUND", "사용자의 쿠폰을 찾을 수 없습니다: " + userCouponId, HttpStatus.NOT_FOUND);
        }
    }

    // Product 관련 예외
    public static class ProductNotFoundException extends BusinessException {
        public ProductNotFoundException(Long productId) {
            super("PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다: " + productId, HttpStatus.NOT_FOUND);
        }
    }

    public static class ProductOutOfStockException extends BusinessException {
        public ProductOutOfStockException(Long productId) {
            super("PRODUCT_OUT_OF_STOCK", "상품 재고가 부족합니다: " + productId, HttpStatus.BAD_REQUEST);
        }
    }

    // Order 관련 예외
    public static class OrderNotFoundException extends BusinessException {
        public OrderNotFoundException(Long orderId) {
            super("ORDER_NOT_FOUND", "찾을 수 없는 주문입니다: " + orderId, HttpStatus.NOT_FOUND);
        }
    }

    public static class InvalidOrderStatusException extends BusinessException {
        public InvalidOrderStatusException(Long orderId) {
            super("INVALID_ORDER_STATUS", "대기 중인 주문만 결제할 수 있습니다", HttpStatus.BAD_REQUEST);
        }
    }

    // Payment 관련 예외
    public static class UnauthorizedPaymentException extends BusinessException {
        public UnauthorizedPaymentException(Long userId) {
            super("UNAUTHORIZED_PAYMENT", "결제는 주문자만 할 수 있습니다", HttpStatus.FORBIDDEN);
        }
    }

}
