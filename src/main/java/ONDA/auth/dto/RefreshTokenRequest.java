package ONDA.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RefreshTokenRequest {
    @NotNull(message = "리프레시토큰을 포함해야합니다")
    private String refreshToken;
}
