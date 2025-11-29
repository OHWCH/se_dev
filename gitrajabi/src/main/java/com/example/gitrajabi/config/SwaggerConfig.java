package com.example.gitrajabi.config;

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
        // API 인증 (Authorization) 설정을 위한 Security Scheme 정의
        // JWT 토큰을 사용하는 API를 위해 Bearer Token 방식을 정의합니다.
        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name(jwtSchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(securityRequirement) // JWT를 기본 인증 방식으로 추가
                .components(components);
    }

    private Info apiInfo() {
        return new Info()
                .title("게시판 & 사용자 인증 API 문서") // 문서 제목
                .description("게시글 CRUD, 댓글 CRUD 및 사용자 JWT 인증/Github OAuth 기능을 포함하는 API 문서입니다.") // 문서 설명
                .version("1.0.0"); // API 버전
    }
}