package ONDA.domain.challenge.service.inf;

import ONDA.domain.challenge.dto.VoteResultResponse;
import ONDA.global.response.ApiResponse;

import java.util.List;

public interface ChallengeVoteService {
    void voteForParticipant(Long voterId, Long challengeId, Long participantId);
    ApiResponse<List<VoteResultResponse>> getVoteResults(Long challengeId);
}
