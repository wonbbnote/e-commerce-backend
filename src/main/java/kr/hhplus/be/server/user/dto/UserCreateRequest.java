package kr.hhplus.be.server.user.dto;

import lombok.Builder;

@Builder
public record UserCreateRequest(String email, String password) {
}
