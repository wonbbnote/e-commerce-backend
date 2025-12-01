package kr.hhplus.be.server.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * 전역 예외 처리 핸들러
 * 모든 Controller에서 발생하는 예외를 중앙에서 처리
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * UserException 처리
     * 각 UserException 내부 클래스가 정의한 HttpStatus를 사용
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleUserException(
            BusinessException e,
            HttpServletRequest request) {

        log.warn("User exception occurred - code: {}, message: {}, path: {}",
                e.getCode(), e.getMessage(), request.getRequestURI());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getCode())
                .message(e.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(errorResponse);
    }

    /**
     * 예상하지 못한 모든 예외 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(
            Exception e,
            HttpServletRequest request) {

        log.error("Unexpected exception occurred - path: {}", request.getRequestURI(), e);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INTERNAL_SERVER_ERROR")
                .message("서버 오류가 발생했습니다")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}

