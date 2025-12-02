package kr.hhplus.be.server.coupon.controller;

import jakarta.validation.Valid;
import kr.hhplus.be.server.coupon.dto.CouponCreateRequest;
import kr.hhplus.be.server.coupon.dto.CouponCreateResponse;
import kr.hhplus.be.server.coupon.dto.CouponListGetResponse;
import kr.hhplus.be.server.coupon.service.CouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 쿠폰 관련 API Controller
 */
@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Slf4j
public class CouponController {

    private final CouponService couponService;

    /**
     * 새로운 쿠폰을 발행한다
     * @param request 쿠폰 생성 요청 DTO
     * @return 생성된 쿠폰 정보
     */
    @PostMapping
    public ResponseEntity<CouponCreateResponse> createCoupon(
            @Valid @RequestBody CouponCreateRequest request) {

        CouponCreateResponse response = couponService.createCoupon(
                request.couponName(),
                request.discountAmount(),
                request.totalQuantity(),
                request.expiredAt()
        );
        return ResponseEntity.status(201).body(response);  // 201 Created
    }

    

    /**
     * 쿠폰 리스트 조회 (페이지네이션)
     * @param page 페이지 번호 (0부터 시작, 기본값: 0)
     * @param size 페이지 크기 (기본값: 10)
     * @param sortBy 정렬 기준 (id, couponName, discountAmount, expiredAt, 기본값: id)
     * @param sortDirection 정렬 방향 (ASC, DESC, 기본값: ASC)
     * @return 쿠폰 리스트
     */
    @GetMapping
    public ResponseEntity<Page<CouponListGetResponse>> getCouponList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {

        // 정렬 필드 검증
        validateSortField(sortBy);

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<CouponListGetResponse> response = couponService.getCouponList(pageable);
        return ResponseEntity.ok(response);
    }


    /**
     * 정렬 필드 검증
     * @param sortBy 정렬 기준 필드
     * @throws IllegalArgumentException 유효하지 않은 정렬 필드
     */
    private void validateSortField(String sortBy) {
        String[] validSortFields = {"id", "couponName", "discountAmount", "totalQuantity", "expiredAt", "createdAt"};

        for (String field : validSortFields) {
            if (field.equals(sortBy)) {
                return;
            }
        }

        log.warn("Invalid sort field requested: {}", sortBy);
        throw new IllegalArgumentException(
                "Invalid sort field. Allowed fields: id, couponName, discountAmount, totalQuantity, expiredAt, createdAt");
    }

}
