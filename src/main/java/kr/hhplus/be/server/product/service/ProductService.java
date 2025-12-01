package kr.hhplus.be.server.product.service;


import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.dto.ProductCreateResponse;
import kr.hhplus.be.server.product.dto.ProductListGetResponse;
import kr.hhplus.be.server.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * 새로운 상품을 생성한다
     * @param productName 상품명
     * @param price 가격 (1 이상)
     * @param stock 재고 (0 이상)
     * @return 생성된 상품 응답 DTO
     * @throws IllegalArgumentException 입력값이 유효하지 않을 때
     */
    @Transactional
    public ProductCreateResponse createProduct(String productName, Integer price, Integer stock) {

        try {
            // 입력값 검증 (DTO 검증 외 추가 검증)
            validateProductInput(productName, price, stock);

            LocalDateTime now = LocalDateTime.now();
            Product newProduct = Product.builder()
                    .productName(productName)
                    .price(price)
                    .stock(stock)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            Product savedProduct = productRepository.save(newProduct);

            return ProductCreateResponse.from(savedProduct);

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create product", e);
        }
    }


    /**
     * 상품 리스트 조회 (페이지네이션)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 상품 리스트
     */
    public Page<ProductListGetResponse> getProductList(Pageable pageable) {

        try {
            Page<Product> products = productRepository.findAll(pageable);

            // Entity → DTO 변환
            Page<ProductListGetResponse> response = products.map(product ->
                    ProductListGetResponse.builder()
                            .productId(product.getId())
                            .productName(product.getProductName())
                            .price(product.getPrice())
                            .stock(product.getStock())
                            .build()
            );
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch product list", e);
        }
    }


    /**
     * 상품 상세 조회
     * @param productId 상품 ID
     * @return 상품 정보
     */
    @Transactional(readOnly = true)
    public ProductListGetResponse getProductById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException.ProductNotFoundException(productId));

        return ProductListGetResponse.from(product);
    }


    /**
     * 상품 입력값 검증
     */
    private void validateProductInput(String productName, Integer price, Integer stock) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (productName.length() > 255) {
            throw new IllegalArgumentException("Product name must be less than 255 characters");
        }

        if (price == null || price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }

        if (stock == null || stock < 0) {
            throw new IllegalArgumentException("Stock must be greater than or equal to 0");
        }
    }
}
