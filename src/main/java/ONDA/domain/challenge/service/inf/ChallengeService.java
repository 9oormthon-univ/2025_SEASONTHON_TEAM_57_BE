package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.dto.ChallengeRequest;

import java.util.List;

public interface ChallengeService {
    void saveChallenge(Long memberId, ChallengeRequest dto);
    List<ChallengeRequest> getAllChallenges(Long memberId);
}
