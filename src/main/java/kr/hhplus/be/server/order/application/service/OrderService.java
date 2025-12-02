package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.balance.domain.Balance;
import kr.hhplus.be.server.balance.dto.BalanceGetResponse;
import kr.hhplus.be.server.balance.service.BalanceService;
import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.UserCoupon;
import kr.hhplus.be.server.coupon.service.UserCouponService;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderItem;
import kr.hhplus.be.server.order.domain.model.Payment;
import kr.hhplus.be.server.order.domain.model.PaymentStatus;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.repository.PaymentRepository;
import kr.hhplus.be.server.order.presentation.request.OrderItemRequest;
import kr.hhplus.be.server.order.presentation.response.OrderCreateResponse;
import kr.hhplus.be.server.order.presentation.response.OrderPayResponse;
import kr.hhplus.be.server.product.domain.Product;
import kr.hhplus.be.server.product.service.ProductService;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserService userService;
    private final ProductService productService;
    private final UserCouponService userCouponService;
    private final BalanceService balanceService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 주문을 생성한다
     * @param userId 결제할 사용자 ID
     * @param items 결제할 주문 상품 목록
     * @param userCouponId 사용할 쿠폰 ID
     * @return 생성된 주문 정보
     * @throws BusinessException 검증 실패 시
     */
    @Transactional
    public OrderCreateResponse createOrder(Long userId, List<OrderItemRequest> items, Long userCouponId){
        // 사용자 검증
        User user = userService.getUserById(userId);

        // 상품 검증 (존재 여부, 재고 가능 여부)
        List<Product> products = new ArrayList<>();
        Integer totalAmount = validateProducts(items, products);

        // 쿠폰 검증 (존재 여부, 만료 여부, 사용 여부)
        UserCoupon userCoupon = null;
        Integer discountAmount = 0;
        if(userCouponId != null){
            userCoupon = userCouponService.getUserCoupon(userCouponId);
            discountAmount = userCoupon.getCoupon().getDiscountAmount();
        }

        // Order 생성 (+ OrderItem)
        Order order = new Order(user, totalAmount, discountAmount);
        for (int i = 0; i < products.size(); i++){
            OrderItem orderItem = new OrderItem(order, products.get(i), items.get(i).quantity());
            order.addOrderItem(orderItem);
        }
        Order savedOrder = orderRepository.save(order);

        // 상품 재고 감소
        for (int i = 0; i < products.size(); i++){
            Product product = products.get(i);
            product.stockDown(items.get(i).quantity());
            productService.updateProduct(product);
        }

        // 쿠폰 사용 처리
        if(userCoupon != null){
            userCouponService.useCoupon(user.getId(), userCoupon.getCoupon().getId());
        }

        // 반환
        return OrderCreateResponse.from(savedOrder);
    }


    private Integer validateProducts(List<OrderItemRequest> items, List<Product> products) {
        Integer totalAmount = 0;
        for (OrderItemRequest item: items){
            Long productId = item.productId();
            Integer quantity = item.quantity();

            Product product = productService.getProductById(productId);
            if(quantity > product.getStock()){
                throw new BusinessException.ProductOutOfStockException(productId);
            }
            products.add(product);
            totalAmount += product.getPrice() * quantity;
        }
        return totalAmount;
    }


    /**
     * 주문 결제를 처리한다
     * @param userId 결제할 사용자 ID
     * @param orderId 결제할 주문 ID
     * @return 결제 결과 정보
     * @throws BusinessException 검증 실패 시
     */
    @Transactional
    public OrderPayResponse payOrder(Long userId, Long orderId) {

        // 주문 검증 (존재 여부, 결제 여부, 사용자)
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new BusinessException.OrderNotFoundException(orderId));

        // 사용자 검증
        User user = userService.getUserById(userId);
        if(!order.getUser().equals(user)){
            new IllegalArgumentException("주문 결제할 수 없는 사용자입니다.");
        }

        // Payment 생성
        LocalDateTime now = LocalDateTime.now();
        Payment payment = new Payment(order, PaymentStatus.SUCCESS, order.getTotalAmount(), now);
        Payment savedPayment = paymentRepository.save(payment);

        // 잔액 차감
        Integer payAmount = payment.getPaymentAmount();
        Balance balance = user.getBalance();
        balance.decrease(payAmount);

        // Order 상태 업데이트
        order.paidSuccess();
        orderRepository.save(order);

        // 반환
        return OrderPayResponse.from(savedPayment);
    }
}
