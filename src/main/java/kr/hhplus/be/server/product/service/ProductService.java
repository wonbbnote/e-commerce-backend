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
     */
    @Transactional
    public ProductCreateResponse createProduct(String productName, Integer price, Integer stock) {
        // 상품 생성
        Product newProduct = new Product(productName, price, stock);
        // DB 저장
        Product savedProduct = productRepository.save(newProduct);
        return ProductCreateResponse.from(savedProduct);
    }


    /**
     * 상품 리스트 조회 (페이지네이션)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 상품 리스트
     */
    public Page<ProductListGetResponse> getProductList(Pageable pageable) {

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
    }


    /**
     * 상품 상세 조회
     * @param productId 상품 ID
     * @return 상품 정보
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException.ProductNotFoundException(productId));
        return product;
    }


    /**
     * 상품 수정
     */
    public void updateProduct(Product product) {
        productRepository.save(product);
    }


}
