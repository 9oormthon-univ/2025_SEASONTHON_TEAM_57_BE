package ONDA.auth.dto;

import ONDA.auth.infra.oauth.dto.TemporaryMemberInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    @Schema(description = "회원가입용 임시 세션", example = "d84bb603-6d19-4be2-8b1b-2316349ae867...")
    private String linkToken; //회원가입용 임시 세션

    private TemporaryMemberInfo memberInfo;

    @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1...")
    private String accessToken;

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1...")
    private String refreshToken;

    // ===== Swagger Doc-only Schemas (실제 런타임에는 사용하지 않음) =====
    @Schema(name = "LoginSuccessDoc", description = "로그인 성공: 토큰만 포함")
    public static class LoginSuccessDoc {
        @Schema(description = "엑세스 토큰", example = "eyJhbGciOiJIUzI1...")
        public String accessToken;
        @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1...refresh")
        public String refreshToken;
    }

    @Schema(name = "LoginSignupRequiredDoc", description = "회원가입 필요: linkToken + memberInfo 포함")
    public static class LoginSignupRequiredDoc {
        @Schema(description = "회원가입용 임시 세션", example = "d84bb603-6d19-4be2-8b1b-2316349ae867...")
        public String linkToken;
        public TemporaryMemberInfo memberInfo;
    }
}
