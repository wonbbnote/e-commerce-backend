package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    /**
     * 사용자가 특정 쿠폰을 이미 발급받았는지 확인
     */
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

    /**
     * 사용자가 발급받은 쿠폰 목록 조회 (페이지네이션)
     */
    Page<UserCoupon> findByUserId(Long userId, Pageable pageable);

    /**
     * 사용자의 특정 쿠폰 조회
     */
    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

}
