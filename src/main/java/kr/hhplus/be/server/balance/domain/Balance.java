package kr.hhplus.be.server.balance.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
     * @param amount 충전할 금액 (양수여야 함)
     */
    public void charge(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Charge amount must be greater than 0");
        }
        this.balance += amount;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 잔액을 차감한다
     * @param amount 충전할 금액 (양수여야 함)
     */
    public void decrease(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Charge amount must be greater than 0");
        }

        if(amount > balance){
            throw new IllegalArgumentException("잔액이 부족합니다");
        }
        this.balance -= amount;
        this.updatedAt = LocalDateTime.now();
    }
}
