package kr.hhplus.be.server.coupon.service;

import kr.hhplus.be.server.coupon.domain.Coupon;
import kr.hhplus.be.server.coupon.dto.CouponCreateResponse;
import kr.hhplus.be.server.coupon.dto.CouponListGetResponse;
import kr.hhplus.be.server.coupon.repository.CouponRepsitory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CouponService 단위 테스트")
class CouponServiceTest {

    @Mock
    private CouponRepsitory couponRepository;

    @InjectMocks
    private CouponService couponService;

    // ======================== createCoupon 테스트 ========================

    @Test
    @DisplayName("유효한 입력으로 쿠폰을 정상적으로 생성한다")
    void createCoupon_success() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        String strExpiredAt = "202512312359"; // 2025-12-31 23:59

        Coupon createdCoupon = new Coupon(couponName, discountAmount, totalQuantity, LocalDateTime.of(2025, 12, 31, 23, 59));
        Coupon savedCoupon = Coupon.builder()
                .id(1L)
                .couponName(createdCoupon.getCouponName())
                .discountAmount(createdCoupon.getDiscountAmount())
                .totalQuantity(createdCoupon.getTotalQuantity())
                .remainingQuantity(createdCoupon.getRemainingQuantity())
                .expiredAt(createdCoupon.getExpiredAt())
                .createdAt(createdCoupon.getCreatedAt())
                .updatedAt(createdCoupon.getUpdatedAt())
                .build();

        when(couponRepository.save(any(Coupon.class))).thenReturn(savedCoupon);

        // when
        CouponCreateResponse response = couponService.createCoupon(couponName, discountAmount, totalQuantity, strExpiredAt);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.couponName()).isEqualTo(couponName);
        assertThat(response.discountAmount()).isEqualTo(discountAmount);
        assertThat(response.totalQuantity()).isEqualTo(totalQuantity);

        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    @Test
    @DisplayName("null 쿠폰명으로 생성할 수 없다")
    void createCoupon_nullCouponName_fail() {
        // given
        String couponName = null;
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        String strExpiredAt = "202512312359";

        // when & then
        assertThatThrownBy(() -> couponService.createCoupon(couponName, discountAmount, totalQuantity, strExpiredAt))
                .hasMessage("쿠폰 이름은 필수 입니다");

        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    @DisplayName("0 이하의 할인 금액으로 생성할 수 없다")
    void createCoupon_zeroDiscountAmount_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 0;
        Integer totalQuantity = 100;
        String strExpiredAt = "202512312359";

        // when & then
        assertThatThrownBy(() -> couponService.createCoupon(couponName, discountAmount, totalQuantity, strExpiredAt))
                .hasMessage("할인 금액은 1원 이상이어야 합니다");

        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    @DisplayName("0 이하의 쿠폰 수량으로 생성할 수 없다")
    void createCoupon_zeroQuantity_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 0;
        String strExpiredAt = "202512312359";

        // when & then
        assertThatThrownBy(() -> couponService.createCoupon(couponName, discountAmount, totalQuantity, strExpiredAt))
                .hasMessage("쿠폰 재고 수량은 1개 이상 이어야 합니다");

        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    @DisplayName("유효하지 않은 날짜 형식으로 생성할 수 없다")
    void createCoupon_invalidDateFormat_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        String strExpiredAt = "2025-12-31 23:59"; // 잘못된 형식

        // when & then
        assertThatThrownBy(() -> couponService.createCoupon(couponName, discountAmount, totalQuantity, strExpiredAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid date format");

        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    @DisplayName("만료 날짜가 과거인 쿠폰으로 생성할 수 없다")
    void createCoupon_expiredAtInPast_fail() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        String strExpiredAt = "202001011200"; // 과거 날짜

        // when & then
        assertThatThrownBy(() -> couponService.createCoupon(couponName, discountAmount, totalQuantity, strExpiredAt))
                .hasMessage("이미 만료된 날짜입니다");

        verify(couponRepository, never()).save(any(Coupon.class));
    }

    @Test
    @DisplayName("생성된 쿠폰이 DB에 저장된다")
    void createCoupon_savedToDatabase() {
        // given
        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;
        String strExpiredAt = "202512312359";

        Coupon createdCoupon = new Coupon(couponName, discountAmount, totalQuantity, LocalDateTime.of(2025, 12, 31, 23, 59));
        Coupon savedCoupon = Coupon.builder()
                .id(1L)
                .couponName(createdCoupon.getCouponName())
                .discountAmount(createdCoupon.getDiscountAmount())
                .totalQuantity(createdCoupon.getTotalQuantity())
                .remainingQuantity(createdCoupon.getRemainingQuantity())
                .expiredAt(createdCoupon.getExpiredAt())
                .createdAt(createdCoupon.getCreatedAt())
                .updatedAt(createdCoupon.getUpdatedAt())
                .build();

        when(couponRepository.save(any(Coupon.class))).thenReturn(savedCoupon);

        // when
        CouponCreateResponse response = couponService.createCoupon(couponName, discountAmount, totalQuantity, strExpiredAt);

        // then
        assertThat(response.id()).isNotNull();
        assertThat(response.id()).isEqualTo(1L);

        verify(couponRepository, times(1)).save(any(Coupon.class));
    }

    // ======================== getCouponList 테스트 ========================

    @Test
    @DisplayName("쿠폰 목록을 정상적으로 조회한다")
    void getCouponList_success() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        Coupon coupon1 = Coupon.builder()
                .id(1L)
                .couponName("신규 사용자 할인")
                .discountAmount(5000)
                .totalQuantity(100)
                .remainingQuantity(100)
                .expiredAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Coupon coupon2 = Coupon.builder()
                .id(2L)
                .couponName("기존 사용자 할인")
                .discountAmount(10000)
                .totalQuantity(50)
                .remainingQuantity(50)
                .expiredAt(LocalDateTime.now().plusDays(30))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Coupon> coupons = List.of(coupon1, coupon2);
        Page<Coupon> couponPage = new PageImpl<>(coupons, pageable, coupons.size());

        when(couponRepository.findAll(pageable)).thenReturn(couponPage);

        // when
        Page<CouponListGetResponse> result = couponService.getCouponList(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(1).id()).isEqualTo(2L);

        verify(couponRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("빈 쿠폰 목록을 조회한다")
    void getCouponList_empty() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Coupon> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(couponRepository.findAll(pageable)).thenReturn(emptyPage);

        // when
        Page<CouponListGetResponse> result = couponService.getCouponList(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();

        verify(couponRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("페이지네이션이 정상적으로 작동한다")
    void getCouponList_pagination() {
        // given
        Pageable pageable = PageRequest.of(1, 2); // 2번째 페이지, 페이지 크기 2

        Coupon coupon3 = Coupon.builder()
                .id(3L)
                .couponName("VIP 할인")
                .discountAmount(20000)
                .totalQuantity(10)
                .remainingQuantity(10)
                .expiredAt(LocalDateTime.now().plusDays(60))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Coupon coupon4 = Coupon.builder()
                .id(4L)
                .couponName("추석 할인")
                .discountAmount(15000)
                .totalQuantity(200)
                .remainingQuantity(200)
                .expiredAt(LocalDateTime.now().plusDays(90))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        List<Coupon> coupons = List.of(coupon3, coupon4);
        Page<Coupon> couponPage = new PageImpl<>(coupons, pageable, 4);

        when(couponRepository.findAll(pageable)).thenReturn(couponPage);

        // when
        Page<CouponListGetResponse> result = couponService.getCouponList(pageable);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(4);
        assertThat(result.getTotalPages()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getSize()).isEqualTo(2);
        assertThat(result.getContent()).hasSize(2);

        verify(couponRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("조회한 쿠폰 정보가 정확하다")
    void getCouponList_returnCorrectInfo() {
        // given
        Pageable pageable = PageRequest.of(0, 10);

        String couponName = "신규 사용자 할인";
        Integer discountAmount = 5000;
        Integer totalQuantity = 100;

        Coupon coupon = Coupon.builder()
                .id(1L)
                .couponName(couponName)
                .discountAmount(discountAmount)
                .totalQuantity(totalQuantity)
                .remainingQuantity(totalQuantity)
                .expiredAt(LocalDateTime.now().plusDays(7))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Page<Coupon> couponPage = new PageImpl<>(List.of(coupon), pageable, 1);

        when(couponRepository.findAll(pageable)).thenReturn(couponPage);

        // when
        Page<CouponListGetResponse> result = couponService.getCouponList(pageable);

        // then
        CouponListGetResponse response = result.getContent().get(0);
        assertThat(response.couponName()).isEqualTo(couponName);
        assertThat(response.discountAmount()).isEqualTo(discountAmount);
        assertThat(response.totalQuantity()).isEqualTo(totalQuantity);
    }
}
