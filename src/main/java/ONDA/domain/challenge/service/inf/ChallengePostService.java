package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.dto.ChallengePostRequest;
import ONDA.domain.challenge.dto.ChallengePostResponse;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.global.response.ApiResponse;

public interface ChallengePostService {
    void saveChallengePost(Long memberId, ChallengePostRequest dto);
    ApiResponse<ChallengePostResponse> getChallengePost(Long challengePostId);
}
