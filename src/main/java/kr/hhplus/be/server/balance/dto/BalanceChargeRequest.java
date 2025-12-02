package kr.hhplus.be.server.balance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record BalanceChargeRequest(
        @NotBlank @Positive Integer amount) {
}
