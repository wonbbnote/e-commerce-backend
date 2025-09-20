package kr.hhplus.be.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.common.Response;
import kr.hhplus.be.server.user.dto.BalanceChargeRequestDto;
import kr.hhplus.be.server.user.dto.BalanceChargeResponseDto;
import kr.hhplus.be.server.user.dto.GetBalanceResponseDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users/balance")
public class BalanceController {

    @PostMapping("/{userId}/charge")
    @Operation(summary = "잔액 충전", description = "결제에 사용될 금액을 충전한다")
    public Response<BalanceChargeResponseDto> chargeBalance(@PathVariable Long userId, @RequestBody BalanceChargeRequestDto request){
        return Response.ok(null);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "잔액 조회", description = "사용자의 잔액을 조회한다")
    public Response<GetBalanceResponseDto> getBalance(@PathVariable Long userId){
        return Response.ok(null);
    }
}
