package knu.chcse.knucseofficialserver.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("KNU CSE Official Server API")
                .description("경북대학교 컴퓨터학부 공식 서버 API 문서")
                .version("v1.0.0"))
            .components(new Components()
                .addSecuritySchemes("StudentNumber", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-Student-Number")
                    .description("학번을 입력하세요 (관리자 권한이 필요한 API에서만 사용)")));
    }
}