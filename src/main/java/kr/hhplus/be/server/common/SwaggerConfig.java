package kr.hhplus.be.server.common;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
    }

    private Info apiInfo() {
        return new Info()
                .title("e-커머스 서비스 API") // API의 제목
                .description("e-커머스 상품 주문 서비스 시나리오에 대한 API 설계입니다.") // API에 대한 설명
                .version("1.0.0"); // API의 버전
    }

}
