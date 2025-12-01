package kr.hhplus.be.server.balance.dto;

import lombok.Builder;

@Builder
public record BalanceGetResponse(Long userId, Integer balance) {
}
