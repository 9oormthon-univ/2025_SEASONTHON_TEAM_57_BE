package ONDA.test;

import ONDA.domain.member.entity.Gender;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DevTestMemberInit implements CommandLineRunner {
    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) {
        Long kakaoId = 123456789L;

        Optional<Member> member = memberRepository.findByKakaoId(kakaoId);
        if (member.isPresent()) {
            return;
        }

        Member m = Member.builder()
                .realName("테스터")
                .nickname("테스트계정")
                .gender(Gender.MALE)
                .birthDate(LocalDate.of(1990, 1, 1))
                .kakaoId(kakaoId)
                .build();
        memberRepository.save(m);
    }
}
