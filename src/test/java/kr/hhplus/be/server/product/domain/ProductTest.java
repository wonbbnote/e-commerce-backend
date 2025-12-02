package kr.hhplus.be.server.product.domain;

import kr.hhplus.be.server.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Product 도메인 단위 테스트")
class ProductTest {

    // ======================== Constructor 테스트 ========================

    @Test
    @DisplayName("유효한 입력으로 상품을 정상적으로 생성한다")
    void product_createSuccess() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;

        // when
        Product product = new Product(productName, price, stock);

        // then
        assertThat(product).isNotNull();
        assertThat(product.getProductName()).isEqualTo(productName);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getStock()).isEqualTo(stock);
        assertThat(product.getCreatedAt()).isNotNull();
        assertThat(product.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("null 상품명으로 생성할 수 없다")
    void product_nullProductName_fail() {
        // given
        String productName = null;
        Integer price = 1500000;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.MissingProductNameException.class)
                .hasMessage("상품 이름은 필수 입니다");
    }

    @Test
    @DisplayName("빈 상품명으로 생성할 수 없다")
    void product_emptyProductName_fail() {
        // given
        String productName = "";
        Integer price = 1500000;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.MissingProductNameException.class)
                .hasMessage("상품 이름은 필수 입니다");
    }

    @Test
    @DisplayName("255자를 초과하는 상품명으로 생성할 수 없다")
    void product_productNameExceedLength_fail() {
        // given
        String productName = "a".repeat(256);
        Integer price = 1500000;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidateProductNameLengthException.class);
    }

    @Test
    @DisplayName("0 이하의 가격으로 생성할 수 없다")
    void product_zeroPrice_fail() {
        // given
        String productName = "노트북";
        Integer price = 0;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidatePriceException.class);
    }

    @Test
    @DisplayName("null 가격으로 생성할 수 없다")
    void product_nullPrice_fail() {
        // given
        String productName = "노트북";
        Integer price = null;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidatePriceException.class);
    }

    @Test
    @DisplayName("음수 가격으로 생성할 수 없다")
    void product_negativePrice_fail() {
        // given
        String productName = "노트북";
        Integer price = -1000;
        Integer stock = 50;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidatePriceException.class);
    }

    @Test
    @DisplayName("음수 재고로 생성할 수 없다")
    void product_negativeStock_fail() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = -10;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidateStockException.class);
    }

    @Test
    @DisplayName("null 재고로 생성할 수 없다")
    void product_nullStock_fail() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = null;

        // when & then
        assertThatThrownBy(() -> new Product(productName, price, stock))
                .isInstanceOf(BusinessException.InvalidateStockException.class);
    }

    @Test
    @DisplayName("0개 재고로 생성할 수 있다")
    void product_zeroStock_success() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 0;

        // when
        Product product = new Product(productName, price, stock);

        // then
        assertThat(product.getStock()).isEqualTo(0);
    }

    // ======================== decreaseStock 테스트 ========================

    @Test
    @DisplayName("정상적으로 재고를 감소시킨다")
    void decreaseStock_success() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;
        Product product = new Product(productName, price, stock);

        Integer quantity = 10;

        // when
        product.decreaseStock(quantity);

        // then
        assertThat(product.getStock()).isEqualTo(40);
    }

    @Test
    @DisplayName("0개로 감소시킬 수 있다")
    void decreaseStock_toZero_success() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 10;
        Product product = new Product(productName, price, stock);

        Integer quantity = 10;

        // when
        product.decreaseStock(quantity);

        // then
        assertThat(product.getStock()).isEqualTo(0);
    }

    @Test
    @DisplayName("0개 수량으로는 재고를 감소시킬 수 없다")
    void decreaseStock_zeroQuantity_fail() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;
        Product product = new Product(productName, price, stock);

        Integer quantity = 0;

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(quantity))
                .isInstanceOf(BusinessException.InvalidQuantityException.class)
                .hasMessage("상품 수량은 1개 이상이어야 합니다");
    }

    @Test
    @DisplayName("음수 수량으로는 재고를 감소시킬 수 없다")
    void decreaseStock_negativeQuantity_fail() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;
        Product product = new Product(productName, price, stock);

        Integer quantity = -5;

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(quantity))
                .isInstanceOf(BusinessException.InvalidQuantityException.class)
                .hasMessage("상품 수량은 1개 이상이어야 합니다");
    }

    @Test
    @DisplayName("null 수량으로는 재고를 감소시킬 수 없다")
    void decreaseStock_nullQuantity_fail() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;
        Product product = new Product(productName, price, stock);

        Integer quantity = null;

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(quantity))
                .isInstanceOf(BusinessException.InvalidQuantityException.class)
                .hasMessage("상품 수량은 1개 이상이어야 합니다");
    }

    @Test
    @DisplayName("재고보다 많은 수량으로는 감소시킬 수 없다")
    void decreaseStock_exceedStock_fail() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 10;
        Product product = new Product(productName, price, stock);

        Integer quantity = 20;

        // when & then
        assertThatThrownBy(() -> product.decreaseStock(quantity))
                .isInstanceOf(BusinessException.ProductOutOfStockException.class);
    }

    @Test
    @DisplayName("여러 번 재고를 감소시킬 수 있다")
    void decreaseStock_multipleTimes() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 100;
        Product product = new Product(productName, price, stock);

        // when
        product.decreaseStock(30);
        product.decreaseStock(25);
        product.decreaseStock(15);

        // then
        assertThat(product.getStock()).isEqualTo(30);
    }

    @Test
    @DisplayName("재고 감소 후 updatedAt이 업데이트된다")
    void decreaseStock_updateTimestamp() {
        // given
        String productName = "노트북";
        Integer price = 1500000;
        Integer stock = 50;
        Product product = new Product(productName, price, stock);

        var originalUpdatedAt = product.getUpdatedAt();

        // when
        try {
            Thread.sleep(10); // 최소한의 시간 경과
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        product.decreaseStock(10);

        // then
        assertThat(product.getUpdatedAt()).isAfter(originalUpdatedAt);
    }
}
