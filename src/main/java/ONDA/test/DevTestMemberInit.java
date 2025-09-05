package ONDA.test;

import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.member.entity.Gender;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.global.category.Category;
import ONDA.global.category.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Profile("dev")
@Component
@RequiredArgsConstructor
public class DevTestMemberInit implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final CategoryRepository categoryRepository;

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

        Category category1 = categoryRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Category category2 = categoryRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Challenge challenge = Challenge.builder()
                .title("첫 번째 챌린지")
                .content("매일 20분 명상하기")
                .image("https://example.com/image2.png")
                .reviewStatus(ReviewStatus.APPROVED)
                .progressStatus(ProgressStatus.ONGOING)
                .startDate(LocalDate.of(2025, 9, 1))
                .endDate(LocalDate.of(2025, 9, 8))
                .createdAt(LocalDateTime.of(2025, 8, 31, 12, 0,1))
                .author(m)
                .build();

        ChallengeCategory cc1 = ChallengeCategory.builder()
                .challenge(challenge)
                .category(category1)
                .build();

        ChallengeCategory cc2 = ChallengeCategory.builder()
                .challenge(challenge)
                .category(category2)
                .build();

        List<ChallengeCategory> categories1 = List.of(cc1, cc2);
        challenge.setCategories(categories1);
        challengeRepository.save(challenge);
    }
}
