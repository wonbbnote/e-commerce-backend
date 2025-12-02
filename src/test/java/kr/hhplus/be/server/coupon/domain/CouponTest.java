package kr.hhplus.be.server.coupon.domain;

import kr.hhplus.be.server.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Coupon 도메인 단위 테스트")
class CouponTest {

    // ======================== Constructor 테스트 ========================

    @Test
    @DisplayName("유효한 입력으로 쿠폰을 정상적으로 생성한다")
    void coupon_createSuccess() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when
        Coupon coupon = new Coupon(couponName, discountAmount, totalQuantity, expiredAt);

        // then
        assertThat(coupon).isNotNull();
        assertThat(coupon.getCouponName()).isEqualTo(couponName);
        assertThat(coupon.getDiscountAmount()).isEqualTo(discountAmount);
        assertThat(coupon.getTotalQuantity()).isEqualTo(totalQuantity);
        assertThat(coupon.getRemainingQuantity()).isEqualTo(totalQuantity);
        assertThat(coupon.getCreatedAt()).isNotNull();
        assertThat(coupon.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("null 쿠폰명으로 생성할 수 없다")
    void coupon_nullCouponName_fail() {
        // given
        String couponName = null;
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.MissingCouponNameException.class)
                .hasMessage("쿠폰 이름은 필수 입니다");
    }

    @Test
    @DisplayName("빈 쿠폰명으로 생성할 수 없다")
    void coupon_emptyCouponName_fail() {
        // given
        String couponName = "";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.MissingCouponNameException.class)
                .hasMessage("쿠폰 이름은 필수 입니다");
    }

    @Test
    @DisplayName("255자를 초과하는 쿠폰명으로 생성할 수 없다")
    void coupon_couponNameExceedLength_fail() {
        // given
        String couponName = "a".repeat(256);
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.InvalidateCouponNameLengthException.class);
    }

    @Test
    @DisplayName("0 이하의 할인 금액으로 생성할 수 없다")
    void coupon_zeroDiscountAmount_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 0;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.InvalidateDiscountAmountException.class);
    }

    @Test
    @DisplayName("null 할인 금액으로 생성할 수 없다")
    void coupon_nullDiscountAmount_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = null;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.InvalidateDiscountAmountException.class);
    }

    @Test
    @DisplayName("0 이하의 쿠폰 수량으로 생성할 수 없다")
    void coupon_zeroQuantity_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 0;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.InvalidateQuantityException.class);
    }

    @Test
    @DisplayName("null 쿠폰 수량으로 생성할 수 없다")
    void coupon_nullQuantity_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = null;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.InvalidateQuantityException.class);
    }

    @Test
    @DisplayName("만료 날짜가 현재보다 이전이면 생성할 수 없다")
    void coupon_expiredAtInPast_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().minusDays(1);

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.AlreadyExpiredException.class);
    }

    @Test
    @DisplayName("만료 날짜가 현재와 같으면 생성할 수 없다")
    void coupon_expiredAtNow_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> new Coupon(couponName, discountAmount, totalQuantity, expiredAt))
                .isInstanceOf(BusinessException.AlreadyExpiredException.class);
    }

    // ======================== isExpired 테스트 ========================

    @Test
    @DisplayName("만료되지 않은 쿠폰은 만료 상태가 false이다")
    void isExpired_notExpired_false() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        Coupon coupon = new Coupon(couponName, discountAmount, totalQuantity, expiredAt);

        // when
        boolean result = coupon.isExpired();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("만료된 쿠폰은 만료 상태가 true이다")
    void isExpired_expired_true() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().minusSeconds(1);
        Coupon coupon = Coupon.builder()
                .id(1L)
                .couponName(couponName)
                .discountAmount(discountAmount)
                .totalQuantity(totalQuantity)
                .remainingQuantity(totalQuantity)
                .expiredAt(expiredAt)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // when
        boolean result = coupon.isExpired();

        // then
        assertThat(result).isTrue();
    }

    // ======================== isAvailable 테스트 ========================

    @Test
    @DisplayName("만료되지 않고 재고가 있으면 발급 가능하다")
    void isAvailable_available_true() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        Coupon coupon = new Coupon(couponName, discountAmount, totalQuantity, expiredAt);

        // when
        boolean result = coupon.isAvailable();

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("만료되었으면 발급 불가능하다")
    void isAvailable_expired_false() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().minusSeconds(1);
        Coupon coupon = Coupon.builder()
                .id(1L)
                .couponName(couponName)
                .discountAmount(discountAmount)
                .totalQuantity(totalQuantity)
                .remainingQuantity(totalQuantity)
                .expiredAt(expiredAt)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // when
        boolean result = coupon.isAvailable();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("재고가 없으면 발급 불가능하다")
    void isAvailable_outOfStock_false() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        Coupon coupon = Coupon.builder()
                .id(1L)
                .couponName(couponName)
                .discountAmount(discountAmount)
                .totalQuantity(totalQuantity)
                .remainingQuantity(0)
                .expiredAt(expiredAt)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // when
        boolean result = coupon.isAvailable();

        // then
        assertThat(result).isFalse();
    }

    // ======================== issue 테스트 ========================

    @Test
    @DisplayName("쿠폰을 정상적으로 발급한다")
    void issue_success() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        Coupon coupon = new Coupon(couponName, discountAmount, totalQuantity, expiredAt);

        // when
        coupon.issue();

        // then
        assertThat(coupon.getRemainingQuantity()).isEqualTo(totalQuantity - 1);
    }

    @Test
    @DisplayName("재고가 없으면 쿠폰을 발급할 수 없다")
    void issue_outOfStock_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        Coupon coupon = Coupon.builder()
                .id(1L)
                .couponName(couponName)
                .discountAmount(discountAmount)
                .totalQuantity(totalQuantity)
                .remainingQuantity(0)
                .expiredAt(expiredAt)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // when & then
        assertThatThrownBy(coupon::issue)
                .isInstanceOf(BusinessException.CouponOutOfStockException.class);
    }

    @Test
    @DisplayName("여러 번 발급하면 재고가 감소한다")
    void issue_multipleIssuance() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 5;
        LocalDateTime expiredAt = LocalDateTime.now().plusDays(7);
        Coupon coupon = new Coupon(couponName, discountAmount, totalQuantity, expiredAt);

        // when
        coupon.issue();
        coupon.issue();
        coupon.issue();

        // then
        assertThat(coupon.getRemainingQuantity()).isEqualTo(2);
    }
}
