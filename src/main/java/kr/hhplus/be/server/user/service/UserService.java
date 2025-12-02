package kr.hhplus.be.server.user.service;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.UserResponse;
import kr.hhplus.be.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User createUser(String email, String password){
        // 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new BusinessException.DuplicateEmailException(email);
        }
        // 새 사용자 생성
        User newUser = User.createNewUser(email, password);
        // DB 저장 (User, Balance 함께 저장)
        User savedUser = userRepository.save(newUser);
        return savedUser;
    }


    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new BusinessException.UserNotFoundException(userId));
        return user;
    }
}
