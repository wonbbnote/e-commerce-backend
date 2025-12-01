package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.common.exception.BusinessException;
import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.domain.UserCoupon;
import kr.hhplus.be.server.coupon.dto.UserCouponResponse;
import kr.hhplus.be.server.coupon.repository.CouponRepsitory;
import kr.hhplus.be.server.coupon.repository.UserCouponRepository;
import kr.hhplus.be.server.user.domain.User;
import kr.hhplus.be.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserCouponService {

    private final UserRepository userRepository;
    private final CouponRepsitory couponRepsitory;
    private final UserCouponRepository userCouponRepository;

    /**
     * 사용자에게 쿠폰을 발급한다
     * @param userId 사용자 ID
     * @param couponId 쿠폰 ID
     * @return 발급된 쿠폰 정보
     * @throws BusinessException 검증 실패 시
     */
    @Transactional
    public UserCouponResponse issueCoupon(Long userId, Long couponId) {

        try {
            // 사용자 존재 여부 확인
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException.UserNotFoundException(userId));

            // 쿠폰 존재 여부 확인
            Coupon coupon = couponRepsitory.findById(couponId)
                    .orElseThrow(() -> new BusinessException.CouponNotFoundException(couponId));

            // 쿠폰이 이미 발급받았는지 확인 (DB 조회 전에 만료/재고 확인하는 것보다 효율적)
            if (userCouponRepository.existsByUserIdAndCouponId(userId, couponId)) {
                throw new BusinessException.CouponAlreadyIssuedException(userId, couponId);
            }

            // 쿠폰 만료 여부 확인
            if (coupon.isExpired()) {
                throw new BusinessException.CouponExpiredException(couponId);
            }

            // 쿠폰 재고 여부 확인
            if (!coupon.isAvailable()) {
                throw new BusinessException.CouponOutOfStockException(couponId);
            }

            // 쿠폰 재고 감소
            coupon.issue();
            couponRepsitory.save(coupon);

            // UserCoupon 생성 및 저장
            LocalDateTime now = LocalDateTime.now();
            UserCoupon userCoupon = UserCoupon.builder()
                    .user(user)
                    .coupon(coupon)
                    .isUsed(false)
                    .createdAt(now)
                    .updatedAt(now)
                    .build();

            UserCoupon savedUserCoupon = userCouponRepository.save(userCoupon);

            return UserCouponResponse.from(savedUserCoupon);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to issue coupon", e);
        }
    }

    /**
     * 사용자가 발급받은 쿠폰 목록 조회 (페이지네이션)
     * @param userId 사용자 ID
     * @param pageable 페이지 정보 (page, size, sort)
     * @return 발급받은 쿠폰 목록
     */
    @Transactional(readOnly = true)
    public Page<UserCouponResponse> getUserCouponList(Long userId, Pageable pageable) {
        Page<UserCoupon> userCoupons = userCouponRepository.findByUserId(userId, pageable);
        return userCoupons.map(UserCouponResponse::from);
    }


    /**
     * 사용자의 쿠폰을 사용한다
     * @param userId 사용자 ID
     * @param couponId 쿠폰 ID
     * @return 사용된 쿠폰 정보
     * @throws BusinessException 쿠폰이 없을 때
     */
    @Transactional
    public UserCouponResponse useCoupon(Long userId, Long couponId) {

        try {
            // 사용자의 쿠폰 조회
            UserCoupon userCoupon = userCouponRepository.findByUserIdAndCouponId(userId, couponId)
                    .orElseThrow(() -> new BusinessException.UserCouponNotFoundException(userId, couponId));

            // 이미 사용된 쿠폰인 경우
            if (userCoupon.getIsUsed()) {
                throw new BusinessException.CouponAlreadyUsedException(couponId);
            }

            // 쿠폰 사용 처리
            userCoupon.use();
            UserCoupon updatedUserCoupon = userCouponRepository.save(userCoupon);

            return UserCouponResponse.from(updatedUserCoupon);

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed to use coupon", e);
        }
    }
}
