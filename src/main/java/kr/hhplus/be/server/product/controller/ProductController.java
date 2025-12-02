package kr.hhplus.be.server.product.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.dto.ProductCreateRequest;
import kr.hhplus.be.server.product.dto.ProductCreateResponse;
import kr.hhplus.be.server.product.dto.ProductListGetResponse;
import kr.hhplus.be.server.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 새로운 상품을 생성한다
     * @param request 상품 생성 요청 DTO
     * @return 생성된 상품 정보
     */
    @PostMapping
    public ResponseEntity<ProductCreateResponse> createProduct(
           @Valid @RequestBody ProductCreateRequest request) {

        ProductCreateResponse response = productService.createProduct(
                request.productName(),
                request.price(),
                request.stock()
        );
        return ResponseEntity.status(201).body(response);  // 201 Created
    }


    /**
     * 상품 리스트 조회 (페이지네이션)
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 (price, createdAt, 기본값: id)
     * @param sortDirection 정렬 방향 (ASC, DESC, 기본값: ASC)
     * @return 상품 리스트
     */
    @GetMapping
    public ResponseEntity<Page<ProductListGetResponse>> getProductList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<ProductListGetResponse> products = productService.getProductList(pageable);
        return ResponseEntity.ok(products);
    }


    /**
     * 상품 상세 조회
     * @param productId 상품 ID
     * @return 상품 정보
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductListGetResponse> getProductById(@PathVariable Long productId) {
        Product product = productService.getProductById(productId);
        ProductListGetResponse response = ProductListGetResponse.from(product);
        return ResponseEntity.ok(response);
    }
}
