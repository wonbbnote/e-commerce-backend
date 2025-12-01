package kr.hhplus.be.server.user.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "password")
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // 현재 암호화 기능 제외

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "balance_id", referencedColumnName = "id", unique = true)
    private Balance balance;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 새로운 사용자를 생성합니다 (Balance 포함)
     * @param email 이메일
     * @param password 암호화된 비밀번호
     * @return 생성된 User 객체
     */
    public static User createNewUser(String email, String password) {
        // 입력 검증
        validateEmail(email);
        validatePassword(password);

        LocalDateTime now = LocalDateTime.now();
        Balance balance = Balance.builder()
                .balance(0)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return User.builder()
                .email(email)
                .password(password)
                .balance(balance)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    /**
     * 이메일 검증
     */
    private static void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("유효한 이메일이 아닙니다");
        }
    }

    /**
     * 비밀번호 검증
     */
    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다");
        }
    }
}
