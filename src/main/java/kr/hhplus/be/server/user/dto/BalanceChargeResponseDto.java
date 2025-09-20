package kr.hhplus.be.server.user.dto;

import java.time.LocalDateTime;

public record BalanceChargeResponseDto(
    Long userId,
    Integer balance,
    LocalDateTime updatedAt
) {
}