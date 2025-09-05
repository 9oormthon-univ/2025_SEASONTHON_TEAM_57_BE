package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.dto.ChallengePostRequest;
import ONDA.domain.challenge.dto.ChallengePostResponse;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.member.dto.MemberResponse;
import ONDA.global.response.ApiResponse;

import java.time.LocalDate;
import java.util.List;

public interface ChallengePostService {
    void saveChallengePost(Long memberId, ChallengePostRequest dto);
    ApiResponse<ChallengePostResponse> getChallengePost(Long challengePostId);
    ApiResponse<List<ChallengePostResponse>> getMyChallengePostsByChallenge(Long memberId, Long challengeId);
    ApiResponse<List<ChallengePostResponse>> getPostsByChallengeAndMember(Long challengeId, Long memberId);
    ApiResponse<List<ChallengePostResponse>> getMyChallengePostsByDate(Long memberId, LocalDate targetDate);
    ApiResponse<List<MemberResponse>> getChallengeParticipants(Long challengeId);
}
