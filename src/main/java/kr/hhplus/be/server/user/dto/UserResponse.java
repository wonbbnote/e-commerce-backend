package kr.hhplus.be.server.user.dto;

import kr.hhplus.be.server.user.domain.User;

import java.time.LocalDateTime;

public record UserResponse(Long id, String email, Integer balance,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getBalance().getBalance(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
