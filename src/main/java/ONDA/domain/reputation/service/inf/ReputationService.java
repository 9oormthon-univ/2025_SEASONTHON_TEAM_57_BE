package ONDA.domain.reputation.service.inf;

import ONDA.domain.reputation.dto.ReputationResponse;
import ONDA.global.response.ApiResponse;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ReputationService {
    void assignReputationScores(Long challengeId);
    //ApiResponse<List<ReputationResponse>> getReputationByCategory(Long memberId, Long categoryId);
    ApiResponse<ReputationResponse> getReputationByCategories(Long memberId);
}
