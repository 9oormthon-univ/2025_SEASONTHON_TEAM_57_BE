package ONDA.auth.controller;

import ONDA.auth.dto.*;
import ONDA.auth.service.AuthService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/kakao/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody KakaoLoginRequest request) {
        ApiResponse<LoginResponse> response = authService.authenticateKakaoUser(request.getCode());
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<LoginResponse>> signup(@RequestBody SignupRequest request) {
        ApiResponse<LoginResponse> response = authService.signup(request);
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String accessToken,
                                       @Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(accessToken, request.getRefreshToken());
        return ResponseEntity.status(200).body(ApiResponse.success(ResponseCode.SUCCESS, null));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AccessTokenResponse>> reissueAccessToken(@Valid @RequestBody RefreshTokenRequest request) {
        AccessTokenResponse response = authService.reissueAccessToken(request.getRefreshToken());
        return ResponseEntity.status(200).body(ApiResponse.success(ResponseCode.SUCCESS, response));
    }
}