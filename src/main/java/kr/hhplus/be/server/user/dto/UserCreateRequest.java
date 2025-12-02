package kr.hhplus.be.server.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotNull
        @Email
        String email,
        @NotNull
        String password) {
}
