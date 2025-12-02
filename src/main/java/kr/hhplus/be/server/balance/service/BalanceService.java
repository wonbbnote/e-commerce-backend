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
     * @throws BusinessException.UserNotFoundException 사용자를 찾을 수 없을 때
     * @throws IllegalArgumentException 충전 금액이 0 이하일 때
     */
    @Transactional
    public BalanceChargeResponse chargeBalance(Long userId, Integer amount) {

        try {
            // 사용자의 현재 잔액 조회
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException.UserNotFoundException(userId));
            Balance balance = user.getBalance();
            if (balance == null) {
                throw new BusinessException.BalanceNotFoundException(userId);
            }

            // 잔액 충전 (도메인 로직)
            balance.charge(amount);

            // DB에 저장
            Balance savedBalance = balanceRepository.save(balance);


            return BalanceChargeResponse.builder()
                    .userId(userId)
                    .balance(savedBalance.getBalance())
                    .updatedAt(savedBalance.getUpdatedAt())
                    .build();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to charge balance", e);
        }
    }


    /**
     * 사용자의 잔액 조회
     * @param userId 사용자 ID
     * @return 사용자의 Balance 객체
     * @throws BusinessException.UserNotFoundException 사용자를 찾을 수 없을 때
     */
    public Balance getBalance(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException.UserNotFoundException(userId));

        Balance balance = user.getBalance();

        // Balance가 null일 경우 예외 발생
        if (balance == null) {
            throw new BusinessException.BalanceNotFoundException(userId);
        }

        return balance;
    }
}
