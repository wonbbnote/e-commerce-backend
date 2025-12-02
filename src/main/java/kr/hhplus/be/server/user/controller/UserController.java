package kr.hhplus.be.server.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.dto.UserCreateRequest;
import kr.hhplus.be.server.user.dto.UserResponse;
import kr.hhplus.be.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "사용자 관리 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "사용자 생성",
            description = "새로운 사용자를 생성합니다. 이메일과 비밀번호는 필수입니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "사용자 생성 성공",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패 (이메일 형식 오류, 이메일 중복 등)",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "409", description = "이메일 중복",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request){

        User savedUser = userService.createUser(
                request.email(), request.password()
        );
        UserResponse response = UserResponse.from(savedUser);
        return ResponseEntity.status(201).body(response);
    }

    @Operation(
            summary = "사용자 조회",
            description = "사용자 ID로 사용자 정보를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 조회 성공",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> get(
            @Parameter(description = "사용자 ID", example = "1")
            @PathVariable("userId") Long userId) {
        User user = userService.getUserById(userId);
        UserResponse response = UserResponse.from(user);
        return ResponseEntity.ok(response);
    }

}
