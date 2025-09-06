package ONDA.test;

import ONDA.domain.challenge.entity.*;
import ONDA.domain.challenge.repository.ChallengePostRepository;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.repository.ChallengeVoteRepository;
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
    private final ChallengePostRepository challengePostRepository;
    private final ChallengeVoteRepository challengeVoteRepository;

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

        Member m2 = Member.builder()
                .realName("테스터2")
                .nickname("테스트계정2")
                .gender(Gender.FEMALE)
                .birthDate(LocalDate.of(1997, 7, 7))
                .kakaoId(kakaoId)
                .build();
        memberRepository.save(m2);

        Challenge challenge1 = addChallenge("첫 번째 챌린지", "매일 20분 명상하기","https://example.com/image2.png",ReviewStatus.APPROVED,
                ProgressStatus.ONGOING, LocalDate.of(2025, 9, 1),LocalDate.of(2025, 9, 8),
                LocalDateTime.of(2025, 8, 31, 12, 0,1), m);

        Challenge challenge2 = addChallenge("두 번째 챌린지", "매일 20분 명상하기","https://example.com/image2.png",ReviewStatus.APPROVED,
                ProgressStatus.ENDED, LocalDate.of(2025, 8, 20),LocalDate.of(2025, 8, 30),
                LocalDateTime.of(2025, 8, 19, 12, 0,1), m);

        Challenge challenge3 = addChallenge("세 번째 챌린지", "매일 20분 명상하기","https://example.com/image2.png",ReviewStatus.APPROVED,
                ProgressStatus.NOT_STARTED, LocalDate.of(2025, 9, 1),LocalDate.of(2025, 9, 8),
                LocalDateTime.of(2025, 8, 31, 12, 0,1), m);

        Challenge challenge4 = addChallenge("네 번째 챌린지", "매일 20분 명상하기","https://example.com/image2.png",ReviewStatus.APPROVED,
                ProgressStatus.ENDED, LocalDate.of(2025, 9, 1),LocalDate.of(2025, 9, 8),
                LocalDateTime.of(2025, 8, 31, 12, 0,1), m);

        addChallengePost(m, challenge1, LocalDate.of(2025, 9, 1));

        addChallengePost(m, challenge2, LocalDate.of(2025, 8, 25));
        addChallengePost(m2, challenge2, LocalDate.of(2025, 8, 25));

        addChallengeVote(challenge2, m, m);
    }
    private Challenge addChallenge(String title, String content, String image,ReviewStatus reviewStatus,
                              ProgressStatus progressStatus, LocalDate startDate, LocalDate endDate,
                              LocalDateTime createdAt, Member author) {
        Challenge challenge = Challenge.builder()
                .title(title)
                .content(content)
                .image(image)
                .reviewStatus(reviewStatus)
                .progressStatus(progressStatus)
                .startDate(startDate)
                .endDate(endDate)
                .createdAt(createdAt)
                .author(author)
                .build();

        Category category1 = categoryRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Category category2 = categoryRepository.findById(2L)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

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

        return challenge;
    }

    public void addChallengePost(Member author, Challenge challenge, LocalDate createDate) {
        ChallengePost challengePost = ChallengePost.builder()
                .author(author)
                .challenge(challenge)
                .createDate(createDate)
                .build();

//        ChallengePostImage challengePostImage = ChallengePostImage.of(challengePost,"https://example.com/image2.png");
//        List<ChallengePostImage> images = List.of(challengePostImage);
//        challengePost.setImages(images);
        challengePostRepository.save(challengePost);
    }

    public void addChallengeVote(Challenge challenge, Member voter, Member participants){
        ChallengeVote challengeVote = ChallengeVote.builder()
                .challenge(challenge)
                .voter(voter)
                .participant(participants)
                .votedAt(LocalDateTime.of(2025, 9, 6, 12, 0,1))
                .build();
        challengeVoteRepository.save(challengeVote);
    }
}
