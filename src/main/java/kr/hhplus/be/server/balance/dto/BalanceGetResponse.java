package kr.hhplus.be.server.balance.dto;

import kr.hhplus.be.server.balance.domain.Balance;
import lombok.Builder;

@Builder
public record BalanceGetResponse(Long userId, Integer balance) {

}
