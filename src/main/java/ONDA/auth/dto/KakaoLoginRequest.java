package ONDA.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoLoginRequest {
    @Schema(description = "카카오 인가 코드", example = "2OUuVJm-ft5C...")
    private String code;
}
