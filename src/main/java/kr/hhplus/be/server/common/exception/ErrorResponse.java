package kr.hhplus.be.server.common.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 에러 응답 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;              // 에러 코드 (예: "USER_NOT_FOUND")
    private String message;           // 에러 메시지
    private LocalDateTime timestamp;  // 발생 시간
    private String path;              // 요청 경로
}
