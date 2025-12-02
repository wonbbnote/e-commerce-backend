package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;

import kr.hhplus.be.server.coupon.dto.CouponCreateResponse;
import kr.hhplus.be.server.coupon.dto.CouponListGetResponse;
import kr.hhplus.be.server.coupon.repository.CouponRepsitory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

    private final CouponRepsitory couponRepsitory;

    /**
     * 새로운 쿠폰을 발행한다
     * @param couponName 쿠폰명
     * @param discountAmount 할인 금액 (1 이상)
     * @param totalQuantity 총 쿠폰 수 (1 이상)
     * @param strExpiredAt 만료 일시 (형식: yyyyMMddHHmm)
     * @return 생성된 쿠폰 응답 DTO
     * @throws IllegalArgumentException 입력값이 유효하지 않을 때
     */
    @Transactional
    public CouponCreateResponse createCoupon(String couponName, Integer discountAmount,
                                             Integer totalQuantity, String strExpiredAt) {
        // 만료 날짜 변환
        LocalDateTime expiredAt = parseExpiredDate(strExpiredAt);
        // 쿠폰 생성
        Coupon newCoupon = new Coupon(couponName, discountAmount, totalQuantity, expiredAt);
        // DB 저장
        Coupon savedCoupon = couponRepsitory.save(newCoupon);
        return CouponCreateResponse.from(savedCoupon);
    }


    /**
     * 쿠폰 리스트를 조회한다 (페이지네이션)
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 쿠폰 리스트 (DTO)
     */
    @Transactional(readOnly = true)
    public Page<CouponListGetResponse> getCouponList(Pageable pageable) {
        Page<Coupon> coupons = couponRepsitory.findAll(pageable);
        return coupons.map(CouponListGetResponse::from);
    }


    /**
     * 만료 날짜 문자열 파싱
     */
    private LocalDateTime parseExpiredDate(String strExpiredAt) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm"); // 문자열 -> LocalDateTime으로 변환
            return LocalDateTime.parse(strExpiredAt, formatter);
        } catch (Exception e) {
            log.error("Invalid date format - expiredAt: {}", strExpiredAt, e);
            throw new IllegalArgumentException(
                    "Invalid date format. Expected format: yyyyMMddHHmm (e.g., 202512312359)", e);
        }
    }
}
