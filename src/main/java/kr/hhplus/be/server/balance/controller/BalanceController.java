package kr.hhplus.be.server.balance.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.balance.domain.Balance;
import kr.hhplus.be.server.balance.dto.BalanceChargeRequest;
import kr.hhplus.be.server.balance.dto.BalanceChargeResponse;
import kr.hhplus.be.server.balance.dto.BalanceGetResponse;
import kr.hhplus.be.server.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Balance", description = "잔액 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @Operation(
            summary = "잔액 충전",
            description = "사용자 잔액을 충전합니다. 충전 금액은 1원 이상의 양수여야 합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 충전 성공",
                    content = @Content(schema = @Schema(implementation = BalanceChargeResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 충전 요청 (금액이 0 이하, 형식 오류 등)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/{userId}/balance")
    public ResponseEntity<BalanceChargeResponse> chargeBalance(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable("userId") Long userId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "충전할 금액",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BalanceChargeRequest.class))
            )
            @RequestBody BalanceChargeRequest request) {

        BalanceChargeResponse response = balanceService.chargeBalance(userId, request.amount());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "잔액 조회",
            description = "사용자의 현재 잔액을 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "잔액 조회 성공",
                    content = @Content(schema = @Schema(implementation = BalanceGetResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{userId}/balance")
    public ResponseEntity<BalanceGetResponse> getBalance(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable("userId") Long userId) {
        BalanceGetResponse response = balanceService.getBalance(userId);
        return ResponseEntity.ok(response);
    }

}
