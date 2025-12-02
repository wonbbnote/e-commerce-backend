package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.balance.domain.Balance;
import kr.hhplus.be.server.balance.service.BalanceService;
import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.UserCoupon;
import kr.hhplus.be.server.coupon.service.UserCouponService;
import kr.hhplus.be.server.order.domain.model.*;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.repository.PaymentRepository;
import kr.hhplus.be.server.order.presentation.request.OrderItemRequest;
import kr.hhplus.be.server.order.presentation.response.OrderCreateResponse;
import kr.hhplus.be.server.order.presentation.response.OrderPayResponse;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.service.ProductService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 단위 테스트")
class OrderServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private ProductService productService;

    @Mock
    private UserCouponService userCouponService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private OrderService orderService;

    // ======================== createOrder 테스트 ========================

    @Test
    @DisplayName("쿠폰 없이 정상적으로 주문을 생성한다")
    void createOrder_withoutCoupon_success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();

        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(10)
                .build();

        OrderItemRequest itemRequest = new OrderItemRequest(1L, 1);

        Order createdOrder = new Order(user, 50000, 0);
        createdOrder = new Order(1L, user, null, OrderStatus.PENDING, 50000, 0, 50000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(createdOrder);

        // when
        OrderCreateResponse response = orderService.createOrder(userId, List.of(itemRequest), null);

        // then
        assertThat(response).isNotNull();
        assertThat(response.totalAmount()).isEqualTo(50000);
        assertThat(response.discountAmount()).isEqualTo(0);

        verify(userService, times(1)).getUserById(userId);
        verify(productService, times(1)).getProductById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productService, times(1)).updateProduct(any(Product.class));
    }

    @Test
    @DisplayName("쿠폰을 포함하여 주문을 생성한다")
    void createOrder_withCoupon_success() {
        // given
        Long userId = 1L;
        Long userCouponId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();

        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(10)
                .build();

        Coupon coupon = new Coupon("할인", 5000, 100, LocalDateTime.now().plusDays(7));

        UserCoupon userCoupon = UserCoupon.builder()
                .id(userCouponId)
                .user(user)
                .coupon(coupon)
                .build();

        OrderItemRequest itemRequest = new OrderItemRequest(1L, 1);

        Order createdOrder = new Order(1L, user, null, OrderStatus.PENDING, 50000, 5000, 45000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);
        when(userCouponService.getUserCoupon(userCouponId)).thenReturn(userCoupon);
        when(orderRepository.save(any(Order.class))).thenReturn(createdOrder);

        // when
        OrderCreateResponse response = orderService.createOrder(userId, List.of(itemRequest), userCouponId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.totalAmount()).isEqualTo(50000);
        assertThat(response.discountAmount()).isEqualTo(5000);
        assertThat(response.finalAmount()).isEqualTo(45000);

        verify(userService, times(1)).getUserById(userId);
        verify(userCouponService, times(1)).getUserCoupon(userCouponId);
        verify(userCouponService, times(1)).useCoupon(userId, coupon.getId());
    }

    @Test
    @DisplayName("여러 개의 상품으로 주문을 생성한다")
    void createOrder_multipleProducts_success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(500000).build())
                .build();

        Product product1 = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(10)
                .build();

        Product product2 = Product.builder()
                .id(2L)
                .productName("마우스")
                .price(30000)
                .stock(20)
                .build();

        OrderItemRequest itemRequest1 = new OrderItemRequest(1L, 1);
        OrderItemRequest itemRequest2 = new OrderItemRequest(2L, 2);

        Order createdOrder = new Order(1L, user, null, OrderStatus.PENDING, 110000, 0, 110000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product1);
        when(productService.getProductById(2L)).thenReturn(product2);
        when(orderRepository.save(any(Order.class))).thenReturn(createdOrder);

        // when
        OrderCreateResponse response = orderService.createOrder(userId, List.of(itemRequest1, itemRequest2), null);

        // then
        assertThat(response).isNotNull();
        assertThat(response.totalAmount()).isEqualTo(110000);

        verify(productService, times(2)).updateProduct(any(Product.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 주문할 수 없다")
    void createOrder_userNotFound_fail() {
        // given
        Long userId = 999L;
        OrderItemRequest itemRequest = new OrderItemRequest(1L, 1);

        when(userService.getUserById(userId)).thenThrow(
                new BusinessException.UserNotFoundException(userId));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(userId, List.of(itemRequest), null))
                .isInstanceOf(BusinessException.UserNotFoundException.class);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("존재하지 않는 상품으로 주문할 수 없다")
    void createOrder_productNotFound_fail() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .build();

        OrderItemRequest itemRequest = new OrderItemRequest(999L, 1);

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductById(999L)).thenThrow(
                new BusinessException.ProductNotFoundException(999L));

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(userId, List.of(itemRequest), null))
                .isInstanceOf(BusinessException.ProductNotFoundException.class);

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("재고가 부족하면 주문할 수 없다")
    void createOrder_outOfStock_fail() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .build();

        Product product = Product.builder()
                .id(1L)
                .productName("노트북")
                .price(50000)
                .stock(5)
                .build();

        OrderItemRequest itemRequest = new OrderItemRequest(1L, 10);

        when(userService.getUserById(userId)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);

        // when & then
        assertThatThrownBy(() -> orderService.createOrder(userId, List.of(itemRequest), null))
                .isInstanceOf(BusinessException.ProductOutOfStockException.class);

        verify(orderRepository, never()).save(any(Order.class));
    }

    // ======================== payOrder 테스트 ========================

    @Test
    @DisplayName("정상적으로 주문을 결제한다")
    void payOrder_success() {
        // given
        Long userId = 1L;
        Long orderId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();

        Order order = new Order(1L, user, null, OrderStatus.PENDING, 50000, 0, 50000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        Payment payment = new Payment(1L, order, PaymentStatus.SUCCESS, 50000, LocalDateTime.now(),
                LocalDateTime.now(), LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userService.getUserById(userId)).thenReturn(user);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // when
        OrderPayResponse response = orderService.payOrder(userId, orderId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.paymentAmount()).isEqualTo(50000);

        verify(orderRepository, times(1)).findById(orderId);
        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("존재하지 않는 주문을 결제할 수 없다")
    void payOrder_orderNotFound_fail() {
        // given
        Long userId = 1L;
        Long orderId = 999L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> orderService.payOrder(userId, orderId))
                .isInstanceOf(BusinessException.OrderNotFoundException.class);

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("다른 사용자의 주문을 결제할 수 없다")
    void payOrder_unauthorizedUser_fail() {
        // given
        Long userId = 1L;
        Long orderId = 1L;

        User owner = User.builder()
                .id(2L)
                .email("owner@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();

        Order order = new Order(1L, owner, null, OrderStatus.PENDING, 50000, 0, 50000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userService.getUserById(userId)).thenReturn(user);

        // when & then
        assertThatThrownBy(() -> orderService.payOrder(userId, orderId))
                .isInstanceOf(BusinessException.UnauthorizedPaymentException.class);

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("이미 결제된 주문은 다시 결제할 수 없다")
    void payOrder_alreadyPaid_fail() {
        // given
        Long userId = 1L;
        Long orderId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();

        Order order = new Order(1L, user, null, OrderStatus.PAID, 50000, 0, 50000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userService.getUserById(userId)).thenReturn(user);

        // when & then
        assertThatThrownBy(() -> orderService.payOrder(userId, orderId))
                .isInstanceOf(BusinessException.InvalidOrderStatusException.class);

        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    @DisplayName("결제 후 Order 상태가 PAID로 변경된다")
    void payOrder_orderStatusChanged() {
        // given
        Long userId = 1L;
        Long orderId = 1L;

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(Balance.builder().balance(100000).build())
                .build();

        Order order = new Order(1L, user, null, OrderStatus.PENDING, 50000, 0, 50000,
                LocalDateTime.now(), LocalDateTime.now(), LocalDateTime.now());

        Payment payment = new Payment(1L, order, PaymentStatus.SUCCESS, 50000, LocalDateTime.now(),
                LocalDateTime.now(), LocalDateTime.now());

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(userService.getUserById(userId)).thenReturn(user);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // when
        orderService.payOrder(userId, orderId);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);

        verify(orderRepository, times(1)).save(order);
    }
}
