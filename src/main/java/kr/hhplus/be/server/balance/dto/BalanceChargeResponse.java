package kr.hhplus.be.server.balance.dto;

import kr.hhplus.be.server.balance.domain.Balance;
import lombok.Builder;

import java.time.LocalDateTime;


public record BalanceChargeResponse(Long userId, Integer balance, LocalDateTime updatedAt) {

    public static BalanceChargeResponse from(Long userId, Balance balance){
        return new BalanceChargeResponse(userId, balance.getBalance(), balance.getUpdatedAt());
    }
}
