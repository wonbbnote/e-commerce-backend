package kr.hhplus.be.server.common;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class SwaggerErrorResponses {

    @Target({ElementType.METHOD, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "충전 금액 오류",
                                            value = "{\"code\": 400, \"message\": \"충전 금액은 양수여야 합니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "잔액 부족",
                                            value = "{\"code\": 400, \"message\": \"잔액이 부족합니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "재고 부족",
                                            value = "{\"code\": 400, \"message\": \"상품의 재고가 부족합니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "쿠폰 소진",
                                            value = "{\"code\": 400, \"message\": \"쿠폰이 모두 소진되었습니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "쿠폰 사용됨",
                                            value = "{\"code\": 400, \"message\": \"이미 사용된 쿠폰입니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "쿠폰 만료",
                                            value = "{\"code\": 400, \"message\": \"만료된 쿠폰입니다.\", \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "사용자 없음",
                                            value = "{\"code\": 404, \"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "상품 없음",
                                            value = "{\"code\": 404, \"message\": \"상품을 찾을 수 없습니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "쿠폰 없음",
                                            value = "{\"code\": 404, \"message\": \"쿠폰을 찾을 수 없습니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "주문 없음",
                                            value = "{\"code\": 404, \"message\": \"찾을 수 없는 주문 정보입니다.\", \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    name = "서버 오류",
                                    value = "{\"code\": 500, \"message\": \"서버 내부 오류가 발생했습니다.\", \"data\": null}"
                            )
                    ))
    })
    public @interface CommonErrorResponses {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "잘못된 충전 요청",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"code\": 400, \"message\": \"충전 금액은 양수여야 합니다.\", \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"서버 내부 오류가 발생했습니다.\", \"data\": null}"
                            )
                    ))
    })
    public @interface ChargeErrorResponses {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "주문 처리 오류",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "재고 부족",
                                            value = "{\"code\": 400, \"message\": \"상품의 재고가 부족합니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "잔액 부족",
                                            value = "{\"code\": 400, \"message\": \"잔액이 부족합니다.\", \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "리소스를 찾을 수 없음",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "사용자 없음",
                                            value = "{\"code\": 404, \"message\": \"사용자를 찾을 수 없습니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "상품 없음",
                                            value = "{\"code\": 404, \"message\": \"상품을 찾을 수 없습니다.\", \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"서버 내부 오류가 발생했습니다.\", \"data\": null}"
                            )
                    ))
    })
    public @interface OrderErrorResponses {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "쿠폰 관련 오류",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = {
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "쿠폰 소진",
                                            value = "{\"code\": 400, \"message\": \"쿠폰이 모두 소진되었습니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "쿠폰 사용됨",
                                            value = "{\"code\": 400, \"message\": \"이미 사용된 쿠폰입니다.\", \"data\": null}"
                                    ),
                                    @io.swagger.v3.oas.annotations.media.ExampleObject(
                                            name = "쿠폰 만료",
                                            value = "{\"code\": 400, \"message\": \"만료된 쿠폰입니다.\", \"data\": null}"
                                    )
                            }
                    )),
            @ApiResponse(responseCode = "404", description = "쿠폰을 찾을 수 없음",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"code\": 404, \"message\": \"쿠폰을 찾을 수 없습니다.\", \"data\": null}"
                            )
                    )),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = "{\"code\": 500, \"message\": \"서버 내부 오류가 발생했습니다.\", \"data\": null}"
                            )
                    ))
    })
    public @interface CouponErrorResponses {
    }
}