package ONDA.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI(
            @Value("${app.backend-base-url}") String baseUrl) {
        return new OpenAPI()
                .info(new Info()
                        .title("ONDA API")
                        .description("ONDA 서비스 API 명세")
                        .version("v1.0.0"))
                .servers(List.of(new Server().url(baseUrl)));
    }
}
