package kr.hhplus.be.server.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.common.Response;
import kr.hhplus.be.server.order.dto.OrderRequestDto;
import kr.hhplus.be.server.order.dto.OrderResponseDto;
import kr.hhplus.be.server.order.dto.PaymentRequestDto;
import kr.hhplus.be.server.order.dto.PaymentResponseDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping()
    @Operation(summary = "주문하기", description = "사용자가 주문한다.")
    public Response<OrderResponseDto> createOrder(@RequestBody OrderRequestDto request){
        return Response.ok(null);
    }

    @PostMapping("/payment")
    @Operation(summary = "결제하기", description = "주문에 대한 결제를 진행한다")
    public Response<PaymentResponseDto> processPayment(@RequestBody PaymentRequestDto request){
        return Response.ok(null);
    }
}
