package ONDA.domain.reputation.controller;

import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.reputation.dto.ReputationResponse;
import ONDA.domain.reputation.service.inf.ReputationService;
import ONDA.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reputations")
@RequiredArgsConstructor
@Tag(name = "Reputation", description = "명성 API")
public class ReputationController {
    private final ReputationService reputationService;

//    @Operation(summary = "사용자의 카테고리별 명성 점수 조회", description = "사용자의 카테고리별 명성 점수 조회를 조회합니다")
//    @GetMapping("/{categoryId}")
//    public ResponseEntity<ApiResponse<List<ReputationResponse>>> getReputationByCategory(@PathVariable("categoryId") Long categoryId,
//                                                                                         @AuthenticationPrincipal Long memberId) {
//        ApiResponse<List<ReputationResponse>> response = reputationService.getReputationByCategory(memberId, categoryId);
//        return ResponseEntity.status(200).body(response);
//    }
}
