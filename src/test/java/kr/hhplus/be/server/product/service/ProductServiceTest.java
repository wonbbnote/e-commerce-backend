package kr.hhplus.be.server.product.service;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.dto.ProductCreateResponse;
import kr.hhplus.be.server.product.dto.ProductListGetResponse;
import kr.hhplus.be.server.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 단위 테스트")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // ======================== createProduct 테스트 ========================

    @Test
    @DisplayName("유효한 입력으로 상품을 정상적으로 생성한다")
    void createProduct_success() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;

        Product createdProduct = new Product(productName, price, stock);
        Product savedProduct = Product.builder()
                .id(1L)
                .productName(createdProduct.getProductName())
                .price(createdProduct.getPrice())
                .stock(createdProduct.getStock())
                .createdAt(createdProduct.getCreatedAt())
                .updatedAt(createdProduct.getUpdatedAt())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // when
        ProductCreateResponse response = productService.createProduct(productName, price, stock);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.productName()).isEqualTo(productName);
        assertThat(response.price()).isEqualTo(price);
        assertThat(response.stock()).isEqualTo(stock);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("null 상품명으로 생성할 수 없다")
    void createProduct_nullProductName_fail() {
        // given
        String productName = null;
        Integer price = 1500000;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> productService.createProduct(productName, price, stock))
                .hasMessage("상품 이름은 필수 입니다");

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("255자를 초과하는 상품명으로 생성할 수 없다")
    void createProduct_productNameExceedLength_fail() {
        // given
        String productName = "a".repeat(256);
        Integer price = 1500000;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> productService.createProduct(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidateProductNameLengthException.class);

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("0 이하의 가격으로 생성할 수 없다")
    void createProduct_zeroPrice_fail() {
        // given
        String productName = "노트북";
        Integer price = 0;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> productService.createProduct(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidatePriceException.class);

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("음수 재고로 생성할 수 없다")
    void createProduct_negativeStock_fail() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = -10;

        // when & then
        assertThatThrownBy(() -> productService.createProduct(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidateStockException.class);

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("생성된 상품이 DB에 저장된다")
    void createProduct_savedToDatabase() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;

        Product createdProduct = new Product(productName, price, stock);
        Product savedProduct = Product.builder()
                .id(1L)
                .productName(createdProduct.getProductName())
                .price(createdProduct.getPrice())
                .stock(createdProduct.getStock())
                .createdAt(createdProduct.getCreatedAt())
                .updatedAt(createdProduct.getUpdatedAt())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // when
        ProductCreateResponse response = productService.createProduct(productName, price, stock);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.id()).isEqualTo(1L);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    // ======================== getProductList 테스트 ========================

    @Test
    @DisplayName("상품 목록을 정상적으로 조회한다")
    void getProductList_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Product product1 = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(1500000)
                .stock(50)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .productName("마우스")
                .price(50000)
                .stock(100)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Product> products = List.of(product1, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // when
        Page<ProductListGetResponse> result = productService.getProductList(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).productId()).isEqualTo(1L);
        assertThat(result.getContent().get(1).productId()).isEqualTo(2L);

        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("빈 상품 목록을 조회한다")
    void getProductList_empty() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        // when
        Page<ProductListGetResponse> result = productService.getProductList(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();

        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("페이지네이션이 정상적으로 작동한다")
    void getProductList_pagination() {
        // given
        Pageable pageable = PageRequest.of(1, 2); // 2번째 페이지, 페이지 크기 2

        Product product3 = Product.builder()
                .id(3L)
                .productName("키보드")
                .price(150000)
                .stock(30)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Product product4 = Product.builder()
                .id(4L)
                .productName("모니터")
                .price(350000)
                .stock(20)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Product> products = List.of(product3, product4);
        Page<Product> productPage = new PageImpl<>(products, pageable, 4);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // when
        Page<ProductListGetResponse> result = productService.getProductList(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);

        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("조회한 상품 정보가 정확하다")
    void getProductList_returnCorrectInfo() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;

        Product product = Product.builder()
                .id(1L)
                .productName(productName)
                .price(price)
                .stock(stock)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Page<Product> productPage = new PageImpl<>(List.of(product), pageable, 1);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        // when
        Page<ProductListGetResponse> result = productService.getProductList(pageable);

        // then
        ProductListGetResponse response = result.getContent().get(0);
        assertThat(response.productName()).isEqualTo(productName);
        assertThat(response.price()).isEqualTo(price);
        assertThat(response.stock()).isEqualTo(stock);
    }

    // ======================== getProductById 테스트 ========================

    @Test
    @DisplayName("존재하는 상품을 ID로 정상적으로 조회한다")
    void getProductById_success() {
        // given
        Long productId = 1L;
        Product product = Product.builder()
                .id(productId)
                .productName("노트북")
                .price(1500000)
                .stock(50)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        Product result = productService.getProductById(productId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getProductName()).isEqualTo("노트북");
        assertThat(result.getPrice()).isEqualTo(1500000);

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("존재하지 않는 상품을 조회하면 ProductNotFoundException이 발생한다")
    void getProductById_productNotFound_fail() {
        // given
        Long productId = 999L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> productService.getProductById(productId))
                .isInstanceOf(BusinessException.ProductNotFoundException.class)
                .hasMessage("상품을 찾을 수 없습니다: " + productId);

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    @DisplayName("조회한 상품 정보가 정확하다")
    void getProductById_returnCorrectInfo() {
        // given
        Long productId = 1L;
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;

        Product product = Product.builder()
                .id(productId)
                .productName(productName)
                .price(price)
                .stock(stock)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // when
        Product result = productService.getProductById(productId);

        // then
        assertThat(result.getId()).isEqualTo(productId);
        assertThat(result.getProductName()).isEqualTo(productName);
        assertThat(result.getPrice()).isEqualTo(price);
        assertThat(result.getStock()).isEqualTo(stock);
    }

    // ======================== updateProduct 테스트 ========================

    @Test
    @DisplayName("상품을 정상적으로 수정한다")
    void updateProduct_success() {
        // given
        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(1500000)
                .stock(40)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productRepository.save(product)).thenReturn(product);

        // when
        productService.updateProduct(product);

        // then
        verify(productRepository, times(1)).save(product);
    }

    @Test
    @DisplayName("수정된 상품이 DB에 저장된다")
    void updateProduct_savedToDatabase() {
        // given
        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(1400000)
                .stock(30)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(productRepository.save(product)).thenReturn(product);

        // when
        productService.updateProduct(product);

        // then
        verify(productRepository, times(1)).save(product);
        assertThat(product.getPrice()).isEqualTo(1400000);
        assertThat(product.getStock()).isEqualTo(30);
    }
}
