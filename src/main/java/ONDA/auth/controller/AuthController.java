package ONDA.auth.controller;

import ONDA.auth.dto.*;
import ONDA.auth.service.AuthService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ExampleObject;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증/인가 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao/login")
    @Operation(summary = "카카오 로그인")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
        responseCode = "200",
        description = "로그인 성공 또는 회원가입 필요",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(oneOf = {
                ONDA.auth.dto.LoginResponse.LoginSuccessDoc.class,
                ONDA.auth.dto.LoginResponse.LoginSignupRequiredDoc.class
            }),
            examples = {
                @ExampleObject(name = "성공", summary = "로그인 성공 (토큰만)", value = """
                {
                  \"code\": \"C000\",
                  \"message\": \"성공\",
                  \"data\": {
                    \"accessToken\": \"eyJ...\",
                    \"refreshToken\": \"eyJ...r\"
                  }
                }
                """),
                @ExampleObject(name = "회원가입 필요", summary = "회원가입 필요", value = """
                {
                  \"code\": \"A002\",
                  \"message\": \"회원가입 필요\",
                  \"data\": {
                    \"linkToken\": \"d84bb......\",
                    \"memberInfo\": {
                      \"isDefaultProfile\": \"false\",
                      \"profile\": \"kakao.example.image.uri\",
                      \"gender\": \"male\"
                    }
                  }
                }
                """)
            }
        )
    )
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody KakaoLoginRequest request) {
        ApiResponse<LoginResponse> response = authService.authenticateKakaoUser(request.getCode());
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    public ResponseEntity<ApiResponse<LoginResponse>> signup(@RequestBody SignupRequest request) {
        ApiResponse<LoginResponse> response = authService.signup(request);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String accessToken,
                                       @Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(accessToken, request.getRefreshToken());
        return ResponseEntity.status(200).body(ApiResponse.success(ResponseCode.SUCCESS, null));
    }
    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 재발급")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> reissueAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        AccessTokenResponse response = authService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.status(200).body(ApiResponse.success(ResponseCode.SUCCESS, response));
    }
}