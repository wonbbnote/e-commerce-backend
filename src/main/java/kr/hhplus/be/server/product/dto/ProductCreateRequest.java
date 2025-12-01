package kr.hhplus.be.server.product.dto;

import lombok.Builder;

public record ProductCreateRequest(String productName, Integer price, Integer stock) {
}
