package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.presentation.request.OrderItemRequest;
import kr.hhplus.be.server.order.presentation.response.OrderCreateResponse;
import kr.hhplus.be.server.order.presentation.response.OrderPayResponse;
import kr.hhplus.be.server.user.domain.User;

import java.util.List;

public class OrderService {

    /**
     * 주문을 생성한다
     * @param request 주문 생성 요청
     * @return 생성된 주문 정보
     * @throws BusinessException 검증 실패 시
     */
    public OrderCreateResponse createOrder(Long userId, List<OrderItemRequest> items, Long userCouponId){

        // 사용자 검증


        // 상품 검증 (존재 여부, 재고 가능 여부)


        // 쿠폰 검증 (존재 여부, 만료 여부, 사용 여부)


        // Order 생성 (+ OrderItem)


        // 상품 재고 감소


        // 쿠폰 사용 처리


        // 반환
        return null;

    }


    /**
     * 주문 결제를 처리한다
     * @param userId 결제할 사용자 ID
     * @param orderId 결제할 주문 ID
     * @return 결제 결과 정보
     * @throws BusinessException 검증 실패 시
     */
    public OrderPayResponse payOrder(Long userId, Long orderId) {

        // 주문 검증 (존재 여부, 결제 여부, 사용자 )

        // Payment 생성

        // 잔액 차감

        // Order 상태 업데이트

        // 반환

        return null;
    }
}
