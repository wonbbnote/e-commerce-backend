package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.user.domain.Balance;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.UserResponse;
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
@DisplayName("UserService 단위테스트")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("사용자를 정상적으로 생성한다.")
    void createUser_success() {
        // given
        String email = "wonbb3313@gmail.com";
        String password = "1234";

        LocalDateTime now = LocalDateTime.now();
        Balance balance = Balance.builder()
                .id(1L)
                .balance(0)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User savedUser = User.builder()
                .id(1L)
                .email(email)
                .password(password)
                .balance(balance)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        UserResponse result = userService.createUser(email, password);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.balance()).isEqualTo(0);

        verify(userRepository).existsByEmail(email);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("사용자 이메일이 존재할 경우 예외를 발생한다.")
    void createUser_duplicatedEmail_fail() {
        // given
        String email = "wonbb3313@gmail.com";
        String password = "1234";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> userService.createUser(email, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 이메일이 존재합니다.");

        verify(userRepository).existsByEmail(email);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("ID로 사용자를 조회한다.")
    void getUserById_success() {
        // given
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        Balance balance = Balance.builder()
                .id(1L)
                .balance(50000)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User user = User.builder()
                .id(userId)
                .email("wonbb3313@gmail.com")
                .password("1234")
                .balance(balance)
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // when
        UserResponse result = userService.getUserById(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.email()).isEqualTo("wonbb3313@gmail.com");
        assertThat(result.balance()).isEqualTo(50000);

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("ID로 사용자 조회 시 존재하지 않으면 예외를 발생한다.")
    void getUserById_notFound_fail() {
        // given
        Long userId = 999L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 사용자입니다.");

        verify(userRepository).findById(userId);
    }
}