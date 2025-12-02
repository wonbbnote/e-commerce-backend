package kr.hhplus.be.server.order.domain.model;

import kr.hhplus.be.server.balance.domain.Balance;
import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Order 도메인 단위 테스트")
class OrderTest {

    // ======================== Constructor 테스트 ========================

    @Test
    @DisplayName("정상적으로 주문을 생성한다")
    void order_createSuccess() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();
        Integer totalAmount = 50000;
        Integer discountAmount = 5000;

        // when
        Order order = new Order(user, totalAmount, discountAmount);

        // then
        assertThat(order).isNotNull();
        assertThat(order.getUser()).isEqualTo(user);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(order.getTotalAmount()).isEqualTo(totalAmount);
        assertThat(order.getDiscountAmount()).isEqualTo(discountAmount);
        assertThat(order.getFinalAmount()).isEqualTo(45000);
        assertThat(order.getOrderItems()).isEmpty();
        assertThat(order.getOrderedAt()).isNotNull();
        assertThat(order.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("할인이 없는 주문을 생성한다")
    void order_createWithoutDiscount() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();
        Integer totalAmount = 50000;
        Integer discountAmount = 0;

        // when
        Order order = new Order(user, totalAmount, discountAmount);

        // then
        assertThat(order.getTotalAmount()).isEqualTo(50000);
        assertThat(order.getDiscountAmount()).isEqualTo(0);
        assertThat(order.getFinalAmount()).isEqualTo(50000);
    }

    @Test
    @DisplayName("초기 상태는 PENDING이다")
    void order_initialStatusPending() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .build();
        Integer totalAmount = 50000;
        Integer discountAmount = 5000;

        // when
        Order order = new Order(user, totalAmount, discountAmount);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("정상적으로 주문 항목을 추가한다")
    void order_addOrderItem_success() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .build();
        Order order = new Order(user, 50000, 0);

        OrderItem orderItem = OrderItem.builder()
                .product(null)
                .quantity(1)
                .unitPrice(50000)
                .build();

        // when
        order.addOrderItem(orderItem);

        // then
        assertThat(order.getOrderItems()).hasSize(1);
        assertThat(order.getOrderItems().get(0)).isEqualTo(orderItem);
        assertThat(orderItem.getOrder()).isEqualTo(order);
    }

    @Test
    @DisplayName("여러 개의 주문 항목을 추가한다")
    void order_addMultipleOrderItems() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .build();
        Order order = new Order(user, 100000, 0);

        OrderItem orderItem1 = OrderItem.builder()
                .quantity(1)
                .unitPrice(50000)
                .build();

        OrderItem orderItem2 = OrderItem.builder()
                .quantity(1)
                .unitPrice(50000)
                .build();

        // when
        order.addOrderItem(orderItem1);
        order.addOrderItem(orderItem2);

        // then
        assertThat(order.getOrderItems()).hasSize(2);
    }

    // ======================== paidSuccess 테스트 ========================

    @Test
    @DisplayName("PENDING 상태에서 결제 완료로 변경한다")
    void paidSuccess_pending_toSuccess() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .build();
        Order order = new Order(user, 50000, 0);

        assertThat(order.getStatus()).isEqualTo(OrderStatus.PENDING);

        // when
        order.paidSuccess();

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    @DisplayName("이미 결제된 주문은 다시 결제할 수 없다")
    void paidSuccess_alreadyPaid_fail() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .build();
        Order order = new Order(user, 50000, 0);
        order.paidSuccess();

        // when & then
        assertThatThrownBy(order::paidSuccess)
                .isInstanceOf(BusinessException.InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("배송 중인 주문은 결제할 수 없다")
    void paidSuccess_shipped_fail() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .build();
        Order order = new Order(1L, user, null, OrderStatus.SHIPPED, 50000, 0, 50000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        // when & then
        assertThatThrownBy(order::paidSuccess)
                .isInstanceOf(BusinessException.InvalidOrderStatusException.class);
    }

    @Test
    @DisplayName("취소된 주문은 결제할 수 없다")
    void paidSuccess_cancelled_fail() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("password123")
                .build();
        Order order = new Order(1L, user, null, OrderStatus.CANCELLED, 50000, 0, 50000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        // when & then
        assertThatThrownBy(order::paidSuccess)
                .isInstanceOf(BusinessException.InvalidOrderStatusException.class);
    }
}
