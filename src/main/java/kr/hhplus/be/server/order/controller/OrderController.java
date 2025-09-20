package kr.hhplus.be.server.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문이 완료되었습니다."),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "상품의 재고가 부족합니다."),
            @ApiResponse(responseCode = "400", description = "쿠폰을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "400", description = "이미 만료된 쿠폰입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류가 발생했습니다.")
    })
    public Response<OrderResponseDto> createOrder(@RequestBody OrderRequestDto request){
        return Response.ok(null);
    }

    @PostMapping("/payment")
    @Operation(summary = "결제하기", description = "주문에 대한 결제를 진행한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "결제가 완료되었습니다."),
            @ApiResponse(responseCode = "404", description = "찾을 수 없는 주문정보입니다."),
            @ApiResponse(responseCode = "400", description = "잔액이 부족합니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류가 발생했습니다.")
    })
    public Response<PaymentResponseDto> processPayment(@RequestBody PaymentRequestDto request){
        return Response.ok(null);
    }
}
