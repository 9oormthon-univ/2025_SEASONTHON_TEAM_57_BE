package ONDA.test;

import ONDA.auth.infra.jwt.JwtProvider;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.entity.Role;
import ONDA.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Profile("dev")
@RestController
@RequestMapping("/api/v1/test")
@Tag(name = "Test", description = "테스트용 API")
@RequiredArgsConstructor
public class TestController {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @GetMapping("/ping")
    @Operation(summary = "서버 확인용 ping-pong")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(type = "string", example = "pong")))
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(200).body("pong");
    }

    @PostMapping("/auth")
    @Operation(summary = "테스트 계정 엑세스토큰 발급")
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1...access")))
    public ResponseEntity<String> auth() {
        Optional<Member> member = memberService.findMember(123456789L);
        return ResponseEntity.status(200)
                .body(jwtProvider.generateAccessToken(member.get().getId(), Role.ROLE_USER));
    }

    @PostMapping("/auth2")
    public ResponseEntity<String> auth2() {
        Optional<Member> member = memberService.findMember(987654321L);
        return ResponseEntity.status(200)
                .body(jwtProvider.generateAccessToken(member.get().getId(), Role.ROLE_USER));
    }
}
