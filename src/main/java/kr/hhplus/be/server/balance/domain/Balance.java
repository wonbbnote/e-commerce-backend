package kr.hhplus.be.server.balance.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.exception.BusinessException;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer balance;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 잔액을 충전한다
     * @param amount 충전할 금액
     */
    public void charge(Integer amount) {
        validateAmount(amount);
        this.balance += amount;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 잔액을 차감한다
     * @param amount 차감할 금액
     */
    public void decrease(Integer amount) {
        validateAmount(amount);
        if(amount > balance){
            throw new BusinessException.InsufficientBalanceException();
        }
        this.balance -= amount;
        this.updatedAt = LocalDateTime.now();
    }


    /**
     * 충전 혹은 차감 금액 양수인지 검증
     * @param amount
     */
    private void validateAmount(Integer amount){
        if (amount == null || amount <= 0) {
            throw new BusinessException.InvalidAmountException();
        }
    }
}
