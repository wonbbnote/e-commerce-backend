package kr.hhplus.be.server.balance.domain;

import kr.hhplus.be.server.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Balance 도메인 단위 테스트")
class BalanceTest {

    @Test
    @DisplayName("포인트를 정상적으로 충전한다.")
    void charge_success() {
        // given
        Integer point = 0;
        Balance balance = Balance.builder().id(1L).balance(point).createdAt(LocalDateTime.now()).updatedAt(null).build();
        Integer chargeAmount = 1000;
        // when
        balance.charge(chargeAmount);
        // then
        assertThat(balance.getBalance()).isEqualTo(1000);
    }

    @Test
    @DisplayName("충전 금액이 음수일 때 예외가 발생한다.")
    void chargeAmountUnderZero_fail() {
        // given
        Integer point = 0;
        Balance balance = Balance.builder().id(1L).balance(point).createdAt(LocalDateTime.now()).updatedAt(null).build();
        Integer chargeAmount = -1000;
        // when & then
        assertThatThrownBy(() -> balance.charge(chargeAmount))
                .isInstanceOf(BusinessException.InvalidAmountException.class)
                .hasMessage("금액은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("포인트를 정상적으로 차감한다.")
    void decrease() {
        // given
        Integer point = 10000;
        Balance balance = Balance.builder().id(1L).balance(point).createdAt(LocalDateTime.now()).updatedAt(null).build();
        Integer decreaseAmount = 1000;
        // when
        balance.decrease(decreaseAmount);
        // then
        assertThat(balance.getBalance()).isEqualTo(9000);
    }

    @Test
    @DisplayName("차감 금액이 음수일 때 예외가 발생한다.")
    void decreaseAmountUnderZero_fail() {
        // given
        Integer point = 0;
        Balance balance = Balance.builder().id(1L).balance(point).createdAt(LocalDateTime.now()).updatedAt(null).build();
        Integer decreaseAmount = -1000;
        // when & then
        assertThatThrownBy(() -> balance.charge(decreaseAmount))
                .isInstanceOf(BusinessException.InvalidAmountException.class)
                .hasMessage("금액은 0보다 커야 합니다.");
    }

    @Test
    @DisplayName("포인트 사용 시 잔고가 부족하면 예외가 발생한다")
    void insufficientBalance_fail() {
        // given
        Integer point = 1000;
        Balance balance = Balance.builder().id(1L).balance(point).createdAt(LocalDateTime.now()).updatedAt(null).build();
        Integer decreaseAmount = 2000;
        // when & then
        assertThatThrownBy(() -> balance.decrease(decreaseAmount))
                .isInstanceOf(BusinessException.InsufficientBalanceException.class)
                .hasMessage("잔액이 부족합니다");
    }
}