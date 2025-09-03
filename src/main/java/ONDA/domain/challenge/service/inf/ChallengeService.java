package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.dto.ChallengeRequestDto;

import java.util.List;

public interface ChallengeService {
    void saveChallenge(Long memberId, ChallengeRequestDto dto);
    List<ChallengeRequestDto> getAllChallenges(Long memberId);
}
