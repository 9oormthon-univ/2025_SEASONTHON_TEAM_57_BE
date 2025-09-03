package ONDA.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @NotNull(message = "리프레시토큰을 포함해야합니다")
    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;
}
