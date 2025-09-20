package kr.hhplus.be.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "충전을 성공했습니다"),
            @ApiResponse(responseCode = "400", description = "충전 금액은 양수여야 합니다."),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류가 발생했습니다.")
    })
    public Response<BalanceChargeResponseDto> chargeBalance(@PathVariable Long userId, @RequestBody BalanceChargeRequestDto request){
        return Response.ok(null);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "잔액 조회", description = "사용자의 잔액을 조회한다")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = ""),
            @ApiResponse(responseCode = "400", description = "사용자를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류가 발생했습니다.")
    })
    public Response<GetBalanceResponseDto> getBalance(@PathVariable Long userId){
        return Response.ok(null);
    }
}
