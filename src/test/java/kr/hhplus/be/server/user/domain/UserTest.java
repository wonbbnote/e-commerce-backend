package kr.hhplus.be.server.user.domain;

import kr.hhplus.be.server.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("User 도메인 단위 테스트")
class UserTest {

    @Test
    @DisplayName("사용자를 정상적으로 생성한다.")
    void createNewUser_success() {
        // given
        String email = "test@example.com";
        String password = "password123";

        // when
        User user = User.createNewUser(email, password);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getPassword()).isEqualTo(password);
        assertThat(user.getBalance()).isNotNull();
        assertThat(user.getBalance().getBalance()).isEqualTo(0);
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("빈 이메일로 사용자를 생성할 수 없다.")
    void createNewUser_emptyEmail_fail() {
        // given
        String email = "";
        String password = "password123";

        // when & then
        assertThatThrownBy(() -> User.createNewUser(email, password))
                .isInstanceOf(BusinessException.MissingEmailException.class)
                .hasMessage("이메일은 필수입니다");
    }

    @Test
    @DisplayName("null 이메일로 사용자를 생성할 수 없다.")
    void createNewUser_nullEmail_fail() {
        // given
        String email = null;
        String password = "password123";

        // when & then
        assertThatThrownBy(() -> User.createNewUser(email, password))
                .isInstanceOf(BusinessException.MissingEmailException.class)
                .hasMessage("이메일은 필수입니다");
    }

    @Test
    @DisplayName("유효하지 않은 이메일 형식으로 사용자를 생성할 수 없다.")
    void createNewUser_invalidEmailFormat_fail() {
        // given
        String email = "invalid-email";
        String password = "password123";

        // when & then
        assertThatThrownBy(() -> User.createNewUser(email, password))
                .isInstanceOf(BusinessException.InvalidEmailException.class)
                .hasMessage("유효한 이메일이 아닙니다");
    }

    @Test
    @DisplayName("빈 비밀번호로 사용자를 생성할 수 없다.")
    void createNewUser_emptyPassword_fail() {
        // given
        String email = "test@example.com";
        String password = "";

        // when & then
        assertThatThrownBy(() -> User.createNewUser(email, password))
                .isInstanceOf(BusinessException.InvalidPasswordException.class)
                .hasMessage("비밀번호는 필수입니다");
    }

    @Test
    @DisplayName("null 비밀번호로 사용자를 생성할 수 없다.")
    void createNewUser_nullPassword_fail() {
        // given
        String email = "test@example.com";
        String password = null;

        // when & then
        assertThatThrownBy(() -> User.createNewUser(email, password))
                .isInstanceOf(BusinessException.InvalidPasswordException.class)
                .hasMessage("비밀번호는 필수입니다");
    }

    @Test
    @DisplayName("생성된 사용자의 Balance가 자동으로 생성된다.")
    void createNewUser_balanceAutoCreated() {
        // given
        String email = "test@example.com";
        String password = "password123";

        // when
        User user = User.createNewUser(email, password);

        // then
        assertThat(user.getBalance()).isNotNull();
        assertThat(user.getBalance().getBalance()).isZero();
        assertThat(user.getBalance().getCreatedAt()).isNotNull();
        assertThat(user.getBalance().getUpdatedAt()).isNotNull();
    }
}
