package kr.hhplus.be.server.balance.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BalanceChargeResponse(Long userId, Integer balance, LocalDateTime updatedAt) {
}
