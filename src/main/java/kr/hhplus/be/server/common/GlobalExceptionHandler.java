package kr.hhplus.be.server.common;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kr.hhplus.be.server.common.exception.BusinessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Response<Void>> handleBusinessException(BusinessException e) {
        return createErrorResponse(e.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<Void>> handleValidationException(MethodArgumentNotValidException e) {
        return createErrorResponse(ErrorCode.INVALID_CHARGE_AMOUNT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Response<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();

        if (message != null) {
            if (message.contains("사용자")) {
                return createErrorResponse(ErrorCode.USER_NOT_FOUND);
            } else if (message.contains("상품")) {
                return createErrorResponse(ErrorCode.PRODUCT_NOT_FOUND);
            } else if (message.contains("쿠폰")) {
                return createErrorResponse(ErrorCode.COUPON_NOT_FOUND);
            } else if (message.contains("주문")) {
                return createErrorResponse(ErrorCode.ORDER_NOT_FOUND);
            } else if (message.contains("충전") || message.contains("양수")) {
                return createErrorResponse(ErrorCode.INVALID_CHARGE_AMOUNT);
            } else if (message.contains("잔액")) {
                return createErrorResponse(ErrorCode.INSUFFICIENT_BALANCE);
            } else if (message.contains("재고")) {
                return createErrorResponse(ErrorCode.INSUFFICIENT_STOCK);
            }
        }

        return createErrorResponse(ErrorCode.USER_NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Response<Void>> handleRuntimeException(RuntimeException e) {
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Void>> handleException(Exception e) {
        return createErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Response<Void>> createErrorResponse(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(Response.error(errorCode));
    }
}