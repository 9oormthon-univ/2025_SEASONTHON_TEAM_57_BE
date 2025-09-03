package ONDA.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccessTokenResponse {
    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;
}
