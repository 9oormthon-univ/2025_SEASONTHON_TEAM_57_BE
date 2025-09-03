package ONDA.domain.challenge.controller;

import ONDA.domain.challenge.dto.ChallengeRequest;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.service.inf.ChallengeService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
@Tag(name = "Challenge", description = "챌린지 API")
public class ChallengeController {
    private final ChallengeService challengeService;

    @Operation(summary = "모든 챌린지 조회", description = "현재 생성된 모든 챌린지를 조회합니다")
    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getAllChallenges(/*@AuthenticationPrincipal CustomUserDetails userDetails*/) {

        Long memberId = 1L;
        ApiResponse<List<ChallengeResponse>> response = challengeService.getAllChallenges(memberId);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "챌린지 생성", description = "게시글 하나를 생성합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "챌린지 생성 성공")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createChallenge(
                                                             /*@AuthenticationPrincipal CustomUserDetails userDetails*/
                                                             @RequestBody ChallengeRequest dto) {
        Long memberId = 1L;
        challengeService.saveChallenge(memberId, dto);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }
}
