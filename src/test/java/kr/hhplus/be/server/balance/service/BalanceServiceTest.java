package kr.hhplus.be.server.balance.service;

import kr.hhplus.be.server.balance.domain.Balance;
import kr.hhplus.be.server.balance.dto.BalanceChargeResponse;
import kr.hhplus.be.server.balance.repository.BalanceRepository;
import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("BalanceService 단위 테스트")
class BalanceServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BalanceRepository balanceRepository;

    @InjectMocks
    private BalanceService balanceService;

    // ======================== chargeBalance 테스트 ========================

    @Test
    @DisplayName("정상적으로 잔액을 충전한다")
    void chargeBalance_success() {
        // given
        Long userId = 1L;
        Integer chargeAmount = 10000;

        Balance balance = Balance.builder()
                .id(1L)
                .balance(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(balance)
                .build();

        Balance updatedBalance = Balance.builder()
                .id(1L)
                .balance(chargeAmount)
                .createdAt(balance.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(balanceRepository.save(any(Balance.class))).thenReturn(updatedBalance);

        // when
        BalanceChargeResponse response = balanceService.chargeBalance(userId, chargeAmount);

        // then
        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo(userId);
        assertThat(response.balance()).isEqualTo(chargeAmount);

        verify(userRepository, times(1)).findById(userId);
        verify(balanceRepository, times(1)).save(any(Balance.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자에게 충전하면 UserNotFoundException이 발생한다")
    void chargeBalance_userNotFound_fail() {
        // given
        Long userId = 999L;
        Integer chargeAmount = 10000;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> balanceService.chargeBalance(userId, chargeAmount))
                .isInstanceOf(BusinessException.UserNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다: " + userId);

        verify(userRepository, times(1)).findById(userId);
        verify(balanceRepository, never()).save(any(Balance.class));
    }

    @Test
    @DisplayName("0 이하의 금액으로 충전하면 InvalidAmountException이 발생한다")
    void chargeBalance_zeroAmount_fail() {
        // given
        Long userId = 1L;
        Integer chargeAmount = 0;

        Balance balance = Balance.builder()
                .id(1L)
                .balance(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(balance)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> balanceService.chargeBalance(userId, chargeAmount))
                .isInstanceOf(BusinessException.InvalidAmountException.class)
                .hasMessage("금액은 0보다 커야 합니다.");

        verify(userRepository, times(1)).findById(userId);
        verify(balanceRepository, never()).save(any(Balance.class));
    }

    @Test
    @DisplayName("음수 금액으로 충전하면 InvalidAmountException이 발생한다")
    void chargeBalance_negativeAmount_fail() {
        // given
        Long userId = 1L;
        Integer chargeAmount = -5000;

        Balance balance = Balance.builder()
                .id(1L)
                .balance(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(balance)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> balanceService.chargeBalance(userId, chargeAmount))
                .isInstanceOf(BusinessException.InvalidAmountException.class)
                .hasMessage("금액은 0보다 커야 합니다.");

        verify(userRepository, times(1)).findById(userId);
        verify(balanceRepository, never()).save(any(Balance.class));
    }

    @Test
    @DisplayName("null 금액으로 충전하면 InvalidAmountException이 발생한다")
    void chargeBalance_nullAmount_fail() {
        // given
        Long userId = 1L;
        Integer chargeAmount = null;

        Balance balance = Balance.builder()
                .id(1L)
                .balance(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(balance)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when & then
        assertThatThrownBy(() -> balanceService.chargeBalance(userId, chargeAmount))
                .isInstanceOf(BusinessException.InvalidAmountException.class)
                .hasMessage("금액은 0보다 커야 합니다.");

        verify(userRepository, times(1)).findById(userId);
        verify(balanceRepository, never()).save(any(Balance.class));
    }

    @Test
    @DisplayName("충전 후 잔액이 정상적으로 업데이트된다")
    void chargeBalance_balanceUpdatedCorrectly() {
        // given
        Long userId = 1L;
        Integer initialBalance = 5000;
        Integer chargeAmount = 10000;
        Integer expectedBalance = initialBalance + chargeAmount;

        Balance balance = Balance.builder()
                .id(1L)
                .balance(initialBalance)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(balance)
                .build();

        Balance updatedBalance = Balance.builder()
                .id(1L)
                .balance(expectedBalance)
                .createdAt(balance.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(balanceRepository.save(any(Balance.class))).thenReturn(updatedBalance);

        // when
        BalanceChargeResponse response = balanceService.chargeBalance(userId, chargeAmount);

        // then
        assertThat(response.balance()).isEqualTo(expectedBalance);
    }

    // ======================== getBalance 테스트 ========================

    @Test
    @DisplayName("정상적으로 사용자의 잔액을 조회한다")
    void getBalance_success() {
        // given
        Long userId = 1L;
        Balance balance = Balance.builder()
                .id(1L)
                .balance(10000)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(balance)
                .build();

        // when
        Balance result = user.getBalance();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualTo(10000);
        assertThat(result.getId()).isEqualTo(1L);

    }

    @Test
    @DisplayName("존재하지 않는 사용자의 잔액을 조회하면 UserNotFoundException이 발생한다")
    void getBalance_userNotFound_fail() {
        // given
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> balanceService.getBalance(userId))
                .isInstanceOf(BusinessException.UserNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다: " + userId);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("조회한 잔액 정보가 정확하다")
    void getBalance_returnCorrectBalance() {
        // given
        Long userId = 1L;
        Integer balanceAmount = 50000;

        Balance balance = Balance.builder()
                .id(1L)
                .balance(balanceAmount)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .balance(balance)
                .build();

        // when
        Balance result = user.getBalance();

        // then
        assertThat(result.getBalance()).isEqualTo(balanceAmount);
        assertThat(result.getId()).isEqualTo(1L);
    }
}
