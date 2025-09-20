package kr.hhplus.be.server.product.dto;

public record ProductResponseDto(
    Long productId,
    String productName,
    Integer price,
    Integer stock

) {
}