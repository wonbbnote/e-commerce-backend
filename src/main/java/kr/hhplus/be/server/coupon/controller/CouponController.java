package kr.hhplus.be.server.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import kr.hhplus.be.server.common.Response;
import kr.hhplus.be.server.coupon.dto.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.dto.CouponIssueResponseDto;
import kr.hhplus.be.server.coupon.dto.UserCouponResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
public class CouponController {

    @PostMapping()
    @Operation(summary = "선착순 쿠폰 발급", description = "선착순으로 쿠폰을 발급한다")
    public Response<CouponIssueResponseDto> issueCoupon(@RequestBody CouponIssueRequestDto request){
        return Response.ok(null);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "보유 쿠폰 조회", description = "사용자의 보유 쿠폰 목록을 조회한다")
    public Response<List<UserCouponResponseDto>> getUserCoupons(@PathVariable Long userId){
        return Response.ok(null);
    }
}
