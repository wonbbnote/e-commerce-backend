package kr.hhplus.be.server.coupon.repository;

import kr.hhplus.be.server.coupon.domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepsitory extends JpaRepository<Coupon, Long> {
}
