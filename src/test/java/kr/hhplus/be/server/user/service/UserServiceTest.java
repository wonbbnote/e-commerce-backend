package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위 테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    // ======================== createUser 테스트 ========================

    @Test
    @DisplayName("유효한 입력으로 사용자를 정상적으로 생성한다")
    void createUser_success() {
        // given
        String email = "test@example.com";
        String password = "password123";

        User createdUser = User.createNewUser(email, password);
        createdUser = User.builder()
                .id(1L)
                .email(createdUser.getEmail())
                .password(createdUser.getPassword())
                .balance(createdUser.getBalance())
                .createdAt(createdUser.getCreatedAt())
                .updatedAt(createdUser.getUpdatedAt())
                .build();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(createdUser);

        // when
        User result = userService.createUser(email, password);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);
        assertThat(result.getBalance()).isNotNull();

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로는 사용자를 생성할 수 없다")
    void createUser_duplicateEmail_fail() {
        // given
        String email = "test@example.com";
        String password = "password123";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.createUser(email, password))
                .isInstanceOf(BusinessException.DuplicateEmailException.class)
                .hasMessage("이미 존재하는 이메일입니다: " + email);

        verify(userRepository, times(1)).existsByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("생성된 사용자가 DB에 정상적으로 저장된다")
    void createUser_savedToDatabase() {
        // given
        String email = "test@example.com";
        String password = "password123";

        User createdUser = User.createNewUser(email, password);
        User savedUser = User.builder()
                .id(1L)
                .email(createdUser.getEmail())
                .password(createdUser.getPassword())
                .balance(createdUser.getBalance())
                .createdAt(createdUser.getCreatedAt())
                .updatedAt(createdUser.getUpdatedAt())
                .build();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        User result = userService.createUser(email, password);

        // then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);

        verify(userRepository, times(1)).save(any(User.class));
    }

    // ======================== getUserById 테스트 ========================

    @Test
    @DisplayName("존재하는 사용자를 ID로 정상적으로 조회한다")
    void getUserById_success() {
        // given
        Long userId = 1L;
        User user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userService.getUserById(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getPassword()).isEqualTo("password123");

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 사용자를 조회하면 UserNotFoundException이 발생한다")
    void getUserById_userNotFound_fail() {
        // given
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(BusinessException.UserNotFoundException.class)
                .hasMessage("사용자를 찾을 수 없습니다: " + userId);

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("조회한 사용자 정보가 정확하다")
    void getUserById_returnCorrectUserInfo() {
        // given
        Long userId = 1L;
        String email = "user@example.com";
        String password = "securePassword";

        User user = User.builder()
                .id(userId)
                .email(email)
                .password(password)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        User result = userService.getUserById(userId);

        // then
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(password);
    }
}
