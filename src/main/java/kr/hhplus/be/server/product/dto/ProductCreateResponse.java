package kr.hhplus.be.server.product.dto;

import kr.hhplus.be.server.product.domain.Product;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProductCreateResponse(
        Long id, String productName, Integer price, Integer stock, LocalDateTime createdAt, LocalDateTime updatedAt) {

    public static ProductCreateResponse from(Product product){
        return ProductCreateResponse.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
