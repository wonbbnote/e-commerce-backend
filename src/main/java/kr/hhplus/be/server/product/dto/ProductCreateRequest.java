package kr.hhplus.be.server.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record ProductCreateRequest(
        @NotBlank String productName,
        @NotNull @Positive Integer price,
        @NotNull @PositiveOrZero Integer stock) {
}
