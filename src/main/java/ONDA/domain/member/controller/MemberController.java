package ONDA.domain.member.controller;

import ONDA.domain.reputation.dto.ReputationResponse;
import ONDA.domain.reputation.service.inf.ReputationService;
import ONDA.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "Profile", description = "프로필 API")
public class MemberController {
    private final ReputationService reputationService;

    @Operation(summary = "사용자 프로필 조회", description = "사용자의 프로필을 조회합니다")
    @GetMapping("/{memberId}")
    public ResponseEntity<ApiResponse<ReputationResponse>> getProfile(@PathVariable("memberId") Long memberId) {
        ApiResponse<ReputationResponse> response = reputationService.getReputationByCategories(memberId);
        return ResponseEntity.status(200).body(response);
    }
}
