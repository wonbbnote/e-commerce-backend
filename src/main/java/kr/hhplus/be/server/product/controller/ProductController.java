package kr.hhplus.be.server.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.common.Response;
import kr.hhplus.be.server.product.dto.ProductResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @GetMapping("")
    @Operation(summary = "전체 상품 리스트 조회", description = "전체 상품 리스트를 조회합니다.")
    public Response<List<ProductResponseDto>> getProductList(){
        return Response.ok(null);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "특정 상품 상세 조회", description = "특정 상품을 조회합니다.")
    public Response<ProductResponseDto> getProduct(@PathVariable Long productId){
        return Response.ok(null);
    }

    @GetMapping("/top")
    @Operation(summary = "상위 상품 조회", description = "3일간 가장 많이 팔린 상위 5개 상품 정보를 제공합니다.")
    public Response<List<ProductResponseDto>> getPopularProductList(){
        return Response.ok(null);
    }
}
