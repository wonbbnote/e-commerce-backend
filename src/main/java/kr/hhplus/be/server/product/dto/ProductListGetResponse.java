package kr.hhplus.be.server.product.dto;

import kr.hhplus.be.server.product.domain.Product;
import lombok.Builder;

@Builder
public record ProductListGetResponse(Long productId, String productName, Integer price, Integer stock) {

    public static ProductListGetResponse from(Product product){
        return ProductListGetResponse.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .price(product.getPrice())
                .stock(product.getStock())
                .build();
    }

}
