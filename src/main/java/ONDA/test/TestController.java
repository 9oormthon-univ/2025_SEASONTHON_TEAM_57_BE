package ONDA.test;

import ONDA.auth.infra.jwt.JwtProvider;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.entity.Role;
import ONDA.domain.member.service.MemberService;
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
@RequiredArgsConstructor
public class TestController {

    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.status(200).body("pong");
    }

    @PostMapping("/auth")
    public ResponseEntity<String> auth() {
        Optional<Member> member = memberService.findMember(123456789L);
        return ResponseEntity.status(200)
                .body(jwtProvider.generateAccessToken(member.get().getId(), Role.ROLE_USER));
    }
}
