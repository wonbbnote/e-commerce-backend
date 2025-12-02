package kr.hhplus.be.server.order.presentation.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.presentation.request.OrderCreateRequest;
import kr.hhplus.be.server.order.presentation.response.OrderCreateResponse;
import kr.hhplus.be.server.order.presentation.response.OrderPayResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 주문을 생성한다
     * @param request 주문 생성 요청
     * @return 생성된 주문 정보
     */
    @PostMapping
    public ResponseEntity<OrderCreateResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        OrderCreateResponse response = orderService.createOrder(request.userId(), request.items(), request.userCouponId());
        return ResponseEntity.status(201).body(response);  // 201 Created
    }

    /**
     * 주문 결제를 처리한다
     * @param userId 결제할 사용자 ID
     * @param orderId 결제할 주문 ID
     * @return 결제 결과 정보
     */
    @PostMapping("{orderId}/payment")
    public ResponseEntity<OrderPayResponse> payOrder(
            @PathVariable("orderId") Long orderId,
            @RequestParam(value = "userId") Long userId) {

        OrderPayResponse response = orderService.payOrder(userId, orderId);
        return ResponseEntity.ok(response);
    }
}
