package ONDA.auth.controller;

import ONDA.auth.dto.KakaoLoginRequest;
import ONDA.auth.dto.LoginResponse;
import ONDA.auth.dto.SignupRequest;
import ONDA.auth.service.AuthService;
import ONDA.global.response.ApiResponse;
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
}