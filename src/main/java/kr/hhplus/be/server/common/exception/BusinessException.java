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
    public static class MissingEmailException extends BusinessException {
        public MissingEmailException() {
            super("MISSING_EMAIL", "이메일은 필수입니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidEmailException extends BusinessException {
        public InvalidEmailException() {
            super("INVALID_EMAIL", "유효한 이메일이 아닙니다", HttpStatus.BAD_REQUEST);
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
        public BalanceNotFoundException() {
            super("BALANCE_NOT_FOUND", "잔액 정보를 찾을 수 없습니다", HttpStatus.NOT_FOUND);
        }
    }

    public static class InvalidAmountException extends BusinessException {
        public InvalidAmountException() {
            super("INVALID_AMOUNT", "금액은 0보다 커야 합니다.", HttpStatus.BAD_REQUEST);
        }

    }

    public static class InsufficientBalanceException extends BusinessException {
        public InsufficientBalanceException() {
            super("INSUFFICIENT_BALANCE", "잔액이 부족합니다", HttpStatus.BAD_REQUEST);
        }
    }


    // Coupon 관련 예외
    public static class MissingCouponNameException extends BusinessException {
        public MissingCouponNameException() {
            super("MISSING_COUPON_NAME", "쿠폰 이름은 필수 입니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidateCouponNameLengthException extends BusinessException {
        public InvalidateCouponNameLengthException() {
            super("INVALID_COUPON_NAME_LENGTH", "쿠폰 이름은 255자 이내여야 합니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidateDiscountAmountException extends BusinessException {
        public InvalidateDiscountAmountException() {
            super("INVALID_DISCOUNT_AMOUNT", "할인 가격은 1원 이상 이어야 합니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidateQuantityException extends BusinessException {
        public InvalidateQuantityException() {
            super("INVALID_QUANTITY", "쿠폰 재고 수량은 1개 이상 이어야 합니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class AlreadyExpiredException extends BusinessException {
        public AlreadyExpiredException() {
            super("ALREADY_EXPIRED", "이미 만료된 날짜입니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class CouponNotFoundException extends BusinessException {
        public CouponNotFoundException(Long couponId) {
            super("COUPON_NOT_FOUND", "쿠폰을 찾을 수 없습니다: " + couponId, HttpStatus.NOT_FOUND);
        }
    }

    public static class CouponOutOfStockException extends BusinessException {
        public CouponOutOfStockException() {
            super("COUPON_OUT_OF_STOCK", "쿠폰 재고가 부족합니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class CouponExpiredException extends BusinessException {
        public CouponExpiredException() {
            super("COUPON_EXPIRED", "만료된 쿠폰입니다", HttpStatus.BAD_REQUEST);
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

    public static class CouponAlreadyIssuedException extends BusinessException {
        public CouponAlreadyIssuedException(Long userId, Long couponId) {
            super("COUPON_ALREADY_ISSUED", "이미 발급받은 쿠폰입니다: " + userId + ", " + couponId, HttpStatus.CONFLICT);
        }
    }

    public static class CouponAlreadyUsedException extends BusinessException {
        public CouponAlreadyUsedException() {
            super("COUPON_ALREADY_USED", "이미 사용된 쿠폰입니다", HttpStatus.CONFLICT);
        }
    }

    // Product 관련 예외
    public static class ProductNotFoundException extends BusinessException {
        public ProductNotFoundException(Long productId) {
            super("PRODUCT_NOT_FOUND", "상품을 찾을 수 없습니다: " + productId, HttpStatus.NOT_FOUND);
        }
    }

    public static class MissingProductNameException extends BusinessException {
        public MissingProductNameException() {
            super("MISSING_PRODUCT_NAME", "상품 이름은 필수 입니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidateProductNameLengthException extends BusinessException {
        public InvalidateProductNameLengthException() {
            super("INVALID_PRODUCT_NAME_LENGTH", "상품 이름은 255자 이내여야 합니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidatePriceException extends BusinessException {
        public InvalidatePriceException() {
            super("INVALID_PRICE", "가격은 1원 이상 이어야 합니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidateStockException extends BusinessException {
        public InvalidateStockException() {
            super("INVALID_STOCK", "재고 수량은 0개 이상 이어야 합니다", HttpStatus.BAD_REQUEST);
        }
    }

    public static class InvalidQuantityException extends BusinessException {
        public InvalidQuantityException() {
            super("INVALID_QUANTITY", "상품 수량은 1개 이상이어야 합니다", HttpStatus.BAD_REQUEST);
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

    // Payment 관련 예외
    public static class UnauthorizedPaymentException extends BusinessException {
        public UnauthorizedPaymentException() {
            super("UNAUTHORIZED_PAYMENT", "결제는 주문자만 할 수 있습니다", HttpStatus.FORBIDDEN);
        }
    }

    public static class InvalidOrderStatusException extends BusinessException {
        public InvalidOrderStatusException() {
            super("INVALID_ORDER_STATUS", "대기 중인 주문만 결제할 수 있습니다", HttpStatus.BAD_REQUEST);
        }
    }

}
