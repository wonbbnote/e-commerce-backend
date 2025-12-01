package kr.hhplus.be.server.coupon.domain;

import jakarta.persistence.*;
import kr.hhplus.be.server.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "user_coupon", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "coupon_id"})
})
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    private Boolean isUsed;
    private LocalDateTime usedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 쿠폰 사용 처리
     */
    public void use() {
        if (isUsed) {
            throw new IllegalArgumentException("Coupon has already been used");
        }
        this.isUsed = true;
        this.usedAt = LocalDateTime.now();
    }

    /**
     * 쿠폰이 사용되었는지 확인
     */
    public boolean isUsed(){
        return isUsed;
    }

}
