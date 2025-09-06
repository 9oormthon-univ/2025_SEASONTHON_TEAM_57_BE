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
import ONDA.domain.reputation.dto.ReputationResponse;
import ONDA.domain.reputation.entity.Reputation;
import ONDA.domain.reputation.repository.ReputationRepository;
import ONDA.domain.reputation.service.inf.ReputationService;
import ONDA.global.category.Category;
import ONDA.global.category.CategoryRepository;
import ONDA.global.exception.BusinessException;
import ONDA.global.exception.ErrorCode;
import ONDA.global.exception.NotFoundMemberException;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReputationServiceImpl implements ReputationService {
    private final ReputationRepository reputationRepository;
    private final ChallengeRepository challengeRepository;
    private final ChallengeVoteRepository challengeVoteRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void assignReputationScores(Long challengeId) {
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_CHALLENGE_FOUND));

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

        boolean isSmallChallenge = results.size() <= 3;

        for (int i = 0; i < results.size(); i++) {
            VoteResultResponse response = results.get(i);
            Member member = memberRepository.findById(response.getParticipantId())
                    .orElseThrow(NotFoundMemberException::new);

            int points;
            if (isSmallChallenge) {
                if (i == 0) points = 25;
                else if (i == 1) points = 20;
                else if (i == 2) points = 15;
                else points = 10;
            } else {
                if (i == 0) points = 50;
                else if (i == 1) points = 35;
                else if (i == 2) points = 20;
                else points = 10;
            }

            for (ChallengeCategory cc : challenge.getCategories()) {
                Category category = cc.getCategory();

                Reputation rep = reputationRepository.findFirstByMemberAndCategory(member, category)
                        .orElseGet(() -> {
                            Reputation newRep = Reputation.builder()
                                    .member(member)
                                    .category(category)
                                    .score(0)
                                    .build();
                            return reputationRepository.save(newRep);
                        });

                rep.addScore(points);
                reputationRepository.save(rep);
            }
        }
    }

//    @Override
//    public ApiResponse<List<ReputationResponse>> getReputationByCategory(Long memberId, Long categoryId) {
//
//        Member member = memberRepository.findById(memberId)
//                .orElseThrow(NotFoundMemberException::new);
//
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(()-> new BusinessException(ErrorCode.NOT_CATEGORY_FOUND));
//
//        List<ReputationResponse> reputations = reputationRepository
//                .findReputationByMemberAndCategory(member, category).stream()
//                .map(ReputationResponse::new)
//                .toList();
//
//        return ApiResponse.success(ResponseCode.SUCCESS, reputations);
//    }

    @Override
    public ApiResponse<ReputationResponse> getReputationByCategories(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(NotFoundMemberException::new);

        List<Category> categories = categoryRepository.findAll();
        List<ReputationResponse.CategoryScore> scoreByCategory = new ArrayList<>();

        for (Category category : categories) {
            Reputation reputation = reputationRepository
                    .findFirstByMemberAndCategory(member, category)
                    .orElse(null);

            int score = (reputation != null) ? reputation.getScore() : 0;

            scoreByCategory.add(new ReputationResponse.CategoryScore(
                    category.getName(),
                    score
            ));
        }
        Integer totalScore1 = reputationRepository.findTotalScoreByMember(memberId);
        int totalScore = (totalScore1 != null) ? totalScore1 : 0;
        ReputationResponse response = new ReputationResponse(member,scoreByCategory,totalScore);
        return ApiResponse.success(ResponseCode.SUCCESS, response);
    }
}
