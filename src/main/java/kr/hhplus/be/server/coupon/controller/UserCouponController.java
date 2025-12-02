package kr.hhplus.be.server.coupon.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.coupon.dto.UserCouponResponse;
import kr.hhplus.be.server.coupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;

    /**
     * 사용자에게 쿠폰을 발급한다 (선착순)
     * @param userId 사용자 ID
     * @param request 쿠폰 발급 요청
     * @return 발급된 쿠폰 정보
     */
    @PostMapping("/{userId}/coupons")
    public ResponseEntity<UserCouponResponse> issueCoupon(@PathVariable("userId") Long userId,
                                                          @Valid @RequestBody CouponIssueRequest request) {
        UserCouponResponse response = userCouponService.issueCoupon(userId, request.couponId());
        return ResponseEntity.status(201).body(response);  // 201 Created
    }


    /**
     * 사용자가 보유한 쿠폰 목록 조회 (페이지네이션)
     * @param userId 사용자 ID
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 (id, couponName, discountAmount, isUsed, usedAt, issuedAt, 기본값: id)
     * @param sortDirection 정렬 방향 (ASC, DESC, 기본값: ASC)
     * @return 사용자가 보유한 쿠폰 목록
     */
    @GetMapping("/{userId}/coupons")
    public ResponseEntity<Page<UserCouponResponse>> getUserCouponList(@PathVariable("userId") Long userId,
                                                                      @RequestParam(name = "page", defaultValue = "0") int page,
                                                                      @RequestParam(name = "size", defaultValue = "10") int size,
                                                                      @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
                                                                      @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        // 정렬 필드 검증
        validateSortField(sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<UserCouponResponse> response = userCouponService.getUserCouponList(userId, pageable);
        return ResponseEntity.ok(response);
    }


    /**
     * 사용자의 쿠폰을 사용한다
     * @param userId 사용자 ID
     * @param couponId 쿠폰 ID
     * @return 사용된 쿠폰 정보
     */
    @PutMapping("/{userId}/coupons/{couponId}")
    public ResponseEntity<UserCouponResponse> useCoupon(@PathVariable("userId") Long userId,
                                                        @PathVariable("couponId") Long couponId) {
        UserCouponResponse response = userCouponService.useCoupon(userId, couponId);
        return ResponseEntity.ok(response);
    }



    /**
     * 정렬 필드 검증
     * @param sortBy 정렬 기준 필드
     * @throws IllegalArgumentException 유효하지 않은 정렬 필드
     */
    private void validateSortField(String sortBy) {
        String[] validSortFields = {"id", "couponName", "discountAmount", "isUsed", "usedAt", "issuedAt"};
        for (String field : validSortFields) {
            if (field.equals(sortBy)) {
                return;
            }
        }
        throw new IllegalArgumentException(
                "Invalid sort field. Allowed fields: id, couponName, discountAmount, isUsed, usedAt, issuedAt");
    }

}
