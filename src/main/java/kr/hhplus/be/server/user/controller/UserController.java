package kr.hhplus.be.server.user.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.UserCreateRequest;
import kr.hhplus.be.server.user.dto.UserResponse;
import kr.hhplus.be.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 새로운 사용자 생성
     * @param request 사용자 생성 요청 (이메일, 비밀번호)
     * @return 생성된 사용자 정보
     */
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request){

        UserResponse response = userService.createUser(
                request.email(), request.password()
        );
        return ResponseEntity.status(201).body(response);
    }

    /**
     * 사용자 조회 (ID 기반)
     * @param userId 사용자 ID
     * @return 조회된 사용자 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> get(@PathVariable("userId") Long userId) {
        UserResponse response = userService.getUserById(userId);
        return ResponseEntity.ok(response);
    }

}
