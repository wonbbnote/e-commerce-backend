package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("OrderItem 도메인 단위 테스트")
class OrderItemTest {

    // ======================== Constructor 테스트 ========================

    @Test
    @DisplayName("정상적으로 주문 항목을 생성한다")
    void orderItem_createSuccess() {
        // given
        Order order = new Order(null, 50000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(10)
                .build();

        Integer quantity = 2;

        // when
        OrderItem orderItem = new OrderItem(order, product, quantity);

        // then
        assertThat(orderItem).isNotNull();
        assertThat(orderItem.getOrder()).isEqualTo(order);
        assertThat(orderItem.getProduct()).isEqualTo(product);
        assertThat(orderItem.getQuantity()).isEqualTo(quantity);
        assertThat(orderItem.getUnitPrice()).isEqualTo(product.getPrice());
        assertThat(orderItem.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("상품 생성 시점의 가격을 저장한다")
    void orderItem_storePriceAtCreationTime() {
        // given
        Order order = new Order(null, 50000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(10)
                .build();

        Integer quantity = 1;

        // when
        OrderItem orderItem = new OrderItem(order, product, quantity);

        // then
        assertThat(orderItem.getUnitPrice()).isEqualTo(50000);
    }

    @Test
    @DisplayName("1개 수량으로 주문 항목을 생성할 수 있다")
    void orderItem_createWithOneQuantity() {
        // given
        Order order = new Order(null, 50000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("마우스")
                .price(50000)
                .stock(100)
                .build();

        // when
        OrderItem orderItem = new OrderItem(order, product, 1);

        // then
        assertThat(orderItem.getQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("다량의 수량으로 주문 항목을 생성할 수 있다")
    void orderItem_createWithLargeQuantity() {
        // given
        Order order = new Order(null, 500000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("마우스")
                .price(50000)
                .stock(100)
                .build();

        // when
        OrderItem orderItem = new OrderItem(order, product, 10);

        // then
        assertThat(orderItem.getQuantity()).isEqualTo(10);
    }

    // ======================== setOrder 테스트 ========================

    @Test
    @DisplayName("Order를 설정한다")
    void orderItem_setOrder() {
        // given
        Order order1 = new Order(null, 50000, 0);
        Order order2 = new Order(null, 100000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(10)
                .build();

        OrderItem orderItem = new OrderItem(order1, product, 1);

        // when
        orderItem.setOrder(order2);

        // then
        assertThat(orderItem.getOrder()).isEqualTo(order2);
    }

    // ======================== calculateFinalPrice 테스트 ========================

    @Test
    @DisplayName("최종 가격을 정상적으로 계산한다")
    void calculateFinalPrice_success() {
        // given
        Order order = new Order(null, 100000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(10)
                .build();

        OrderItem orderItem = new OrderItem(order, product, 2);

        // when
        Integer finalPrice = orderItem.calculateFinalPrice();

        // then
        assertThat(finalPrice).isEqualTo(100000); // 50000 * 2
    }

    @Test
    @DisplayName("수량이 1개일 때 최종 가격은 단가와 같다")
    void calculateFinalPrice_oneQuantity() {
        // given
        Order order = new Order(null, 50000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("마우스")
                .price(50000)
                .stock(100)
                .build();

        OrderItem orderItem = new OrderItem(order, product, 1);

        // when
        Integer finalPrice = orderItem.calculateFinalPrice();

        // then
        assertThat(finalPrice).isEqualTo(50000);
    }

    @Test
    @DisplayName("다량 수량의 최종 가격을 계산한다")
    void calculateFinalPrice_largeQuantity() {
        // given
        Order order = new Order(null, 1000000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("키보드")
                .price(150000)
                .stock(50)
                .build();

        OrderItem orderItem = new OrderItem(order, product, 5);

        // when
        Integer finalPrice = orderItem.calculateFinalPrice();

        // then
        assertThat(finalPrice).isEqualTo(750000); // 150000 * 5
    }

    @Test
    @DisplayName("저가 상품의 최종 가격을 계산한다")
    void calculateFinalPrice_lowPrice() {
        // given
        Order order = new Order(null, 50000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("볼펜")
                .price(1000)
                .stock(1000)
                .build();

        OrderItem orderItem = new OrderItem(order, product, 10);

        // when
        Integer finalPrice = orderItem.calculateFinalPrice();

        // then
        assertThat(finalPrice).isEqualTo(10000); // 1000 * 10
    }

    @Test
    @DisplayName("고가 상품의 최종 가격을 계산한다")
    void calculateFinalPrice_highPrice() {
        // given
        Order order = new Order(null, 5000000, 0);

        Product product = Product.builder()
                .id(1L)
                .productName("명품 노트북")
                .price(3000000)
                .stock(5)
                .build();

        OrderItem orderItem = new OrderItem(order, product, 2);

        // when
        Integer finalPrice = orderItem.calculateFinalPrice();

        // then
        assertThat(finalPrice).isEqualTo(6000000); // 3000000 * 2
    }
}
