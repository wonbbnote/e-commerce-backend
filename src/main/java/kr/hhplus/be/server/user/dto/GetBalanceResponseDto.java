package kr.hhplus.be.server.user.dto;

public record GetBalanceResponseDto(
    Long userId,
    Integer balance
) {
}