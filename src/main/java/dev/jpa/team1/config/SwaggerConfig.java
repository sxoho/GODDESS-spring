package dev.jpa.team1.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        // JWT 인증 스키마 이름
        String jwtSchemeName = "jwtAuth";

        // API 문서 정보 설정
        Info info = new Info()
                .title("서비스 API 문서")
                .version("v1.0.0")
                .description("Spring Boot 3.x & JWT API 명세서");

        // Authorization Header 스키마 설정 (Bearer Token)
        SecurityScheme securityScheme = new SecurityScheme()
                .name(jwtSchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");

        // 모든 API에 보안 요구사항 적용 (필요 시 선택 적용도 가능)
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(new Components().addSecuritySchemes(jwtSchemeName, securityScheme));
    }
}