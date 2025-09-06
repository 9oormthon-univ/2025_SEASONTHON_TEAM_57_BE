package ONDA.domain.reputation.service.impl;

import ONDA.domain.challenge.dto.VoteResultResponse;
import ONDA.domain.challenge.entity.Challenge;
import ONDA.domain.challenge.entity.ChallengeCategory;
import ONDA.domain.challenge.entity.ChallengePost;
import ONDA.domain.challenge.repository.ChallengePostRepository;
import ONDA.domain.challenge.repository.ChallengeRepository;
import ONDA.domain.challenge.repository.ChallengeVoteRepository;
import ONDA.domain.member.entity.Member;
import ONDA.domain.member.repository.MemberRepository;
import ONDA.domain.reputation.entity.Reputation;
import ONDA.domain.reputation.repository.ReputationRepository;
import ONDA.domain.reputation.service.inf.ReputationService;
import ONDA.global.category.Category;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReputationServiceImpl implements ReputationService {
    private final ReputationRepository reputationRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengePostRepository challengePostRepository;
    private final ChallengeVoteRepository challengeVoteRepository;
    private final MemberRepository memberRepository;

    public void assignReputationScores(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

        // 1. 해당 챌린지 인증글 가져오기 (투표 수 포함)
        List<ChallengePost> posts = challengePostRepository.findByChallenge(challenge);

        // 2. 투표수 기준 정렬
        List<Member> participants = challenge.getChallengePosts().stream()
                .map(ChallengePost::getAuthor)
                .distinct()
                .toList();

        List<VoteResultResponse> results = participants.stream()
                .map(p -> new VoteResultResponse(
                        p.getId(),
                        p.getNickname(),
                        p.getProfile(),
                        challengeVoteRepository.countByChallengeAndParticipant(challenge, p),
                        0
                ))
                .sorted(Comparator.comparingLong(VoteResultResponse::getVoteCount).reversed())
                .toList();


        // 3. 순위 매기기 및 점수 부여
        for (int i = 0; i < results.size(); i++) {
            VoteResultResponse response = results.get(i);
            Member member = memberRepository.findById(response.getParticipantId())
                    .orElseThrow(NotFoundMemberException::new);

            int points;
            if (i == 0) points = 50;     // 1등
            else if (i == 1) points = 35; // 2등
            else if (i == 2) points = 20; // 3등
            else points = 10;             // 기타 참여자

            // 카테고리별 점수 누적
            for (ChallengeCategory cc : challenge.getCategories()) {
                Category category = cc.getCategory();

                Reputation rep = reputationRepository.findByMemberAndCategory(member, category)
                        .orElseGet(() -> {
                            Reputation newRep = Reputation.builder()
                                    .member(member)
                                    .category(category)
                                    .build();
                            return reputationRepository.save(newRep);
                        });

                rep.addScore(points);
                reputationRepository.save(rep);
            }
        }
    }

}
