package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.dto.ChallengeRequest;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.global.response.ApiResponse;

import java.util.List;

public interface ChallengeService {
    void saveChallenge(Long memberId, ChallengeRequest dto);
    ApiResponse<List<ChallengeResponse>> getAllChallenges(Long memberId);
}
