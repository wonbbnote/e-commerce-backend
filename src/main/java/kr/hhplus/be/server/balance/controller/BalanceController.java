package kr.hhplus.be.server.balance.controller;


import kr.hhplus.be.server.balance.domain.Balance;
import kr.hhplus.be.server.balance.dto.BalanceChargeRequest;
import kr.hhplus.be.server.balance.dto.BalanceChargeResponse;
import kr.hhplus.be.server.balance.dto.BalanceGetResponse;
import kr.hhplus.be.server.balance.service.BalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    /**
     * 사용자의 잔액 충전
     * @param userId 사용자 ID
     * @param request 충전 요청 DTO (amount: 양수)
     * @return 충전 후 응답 DTO
     */
    @PostMapping("/{userId}/balance")
    public ResponseEntity<BalanceChargeResponse> chargeBalance(
            @PathVariable("userId") Long userId,
            @RequestBody BalanceChargeRequest request) {

        BalanceChargeResponse response = balanceService.chargeBalance(userId, request.amount());
        return ResponseEntity.ok(response);
    }


    /**
     * 사용자의 잔액 조회
     * @param userId 사용자 ID
     * @return 사용자 ID와 잔액 정보
     */
    @GetMapping("/{userId}/balance")
    public ResponseEntity<BalanceGetResponse> getBalance(@PathVariable("userId") Long userId) {
        BalanceGetResponse response = balanceService.getBalance(userId);
        return ResponseEntity.ok(response);
    }

}
