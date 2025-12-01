package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.UserResponse;
import kr.hhplus.be.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(String email, String password){

        // 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("같은 이메일이 존재합니다.");
        }
        // 새 사용자 생성
        User newUser = User.createNewUser(email, password);
        // DB 저장 (User, Balance 함께 저장)
        User savedUser = userRepository.save(newUser);
        // 반환값 변환
        return UserResponse.from(savedUser);
    }


    public UserResponse getUserById(Long userId) {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        return UserResponse.from(user);
    }
}
