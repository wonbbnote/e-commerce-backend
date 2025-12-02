package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.domain.Balance;
import kr.hhplus.be.server.balance.dto.BalanceChargeResponse;
import kr.hhplus.be.server.balance.dto.BalanceGetResponse;
import kr.hhplus.be.server.balance.repository.BalanceRepository;
import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BalanceService {

    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;

    /**
     * 사용자의 잔액을 충전한다
     * @param userId 사용자 ID
     * @param amount 충전할 금액 (양수여야 함)
     * @return 충전 후 응답 DTO
     */
    @Transactional
    public BalanceChargeResponse chargeBalance(Long userId, Integer amount) {
        // 사용자의 현재 잔액 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException.UserNotFoundException(userId));
        Balance balance = user.getBalance();
        // 잔액 충전
        balance.charge(amount);
        // DB에 저장
        Balance savedBalance = balanceRepository.save(balance);
        return BalanceChargeResponse.from(userId, savedBalance);
    }


    /**
     * 사용자의 잔액을 조회한다
     * @param userId 사용자 ID
     * @return 사용자의 BalanceGetResponse DTO
     */
    public BalanceGetResponse getBalance(Long userId) {
        // 사용자 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException.UserNotFoundException(userId));
        // 잔액 확인
        Balance balance = user.getBalance();
        return BalanceGetResponse.builder().userId(userId).balance(balance.getBalance()).build();
    }
}
