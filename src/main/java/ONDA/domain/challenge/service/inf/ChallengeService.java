package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.dto.ChallengeRequest;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.global.response.ApiResponse;

import java.util.List;

public interface ChallengeService {
    void saveChallenge(Long memberId, ChallengeRequest dto);
    ApiResponse<List<ChallengeResponse>> getAllChallenges();
    ApiResponse<List<ChallengeResponse>> getMyChallenges(Long memberId);
    ApiResponse<List<ChallengeResponse>> getMyChallengePosts(Long memberId);
    ApiResponse<List<ChallengeResponse>> getChallengesByReviewStatus(ReviewStatus reviewStatus);
    ApiResponse<List<ChallengeResponse>> getChallengesByProgressStatus(ProgressStatus progressStatus);
    ApiResponse<List<ChallengeResponse>> getChallengesByCategory(Long categoryId);
    ApiResponse<ChallengeResponse> getChallenge(Long challengeId);
    ApiResponse<List<ChallengeResponse>> getOngoingChallengesOrderByParticipants();
    void pendingChallenge(Long challengeId);
    void approveChallenge(Long challengeId);
    void rejectChallenge(Long challengeId);
}
