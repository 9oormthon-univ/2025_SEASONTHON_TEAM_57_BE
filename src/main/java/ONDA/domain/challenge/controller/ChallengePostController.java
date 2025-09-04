package ONDA.domain.challenge.controller;

import ONDA.domain.challenge.dto.ChallengePostRequest;
import ONDA.domain.challenge.dto.ChallengePostResponse;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.service.inf.ChallengePostService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/challenge-posts")
@RequiredArgsConstructor
@Tag(name = "ChallengePost", description = "챌린지 인증글 API")
public class ChallengePostController {
    private final ChallengePostService challengePostService;

    @Operation(summary = "챌린지 인증글 생성", description = "챌린지 인증글 하나를 생성합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "챌린지 인증글 생성 성공")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createChallengePost(@AuthenticationPrincipal Long memberId,
                                                             @RequestBody ChallengePostRequest dto) {
        challengePostService.saveChallengePost(memberId, dto);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }

    @Operation(summary = "챌린지 인증글 조회", description = "챌린지 인증글 하나를 조회합니다")
    @GetMapping("/{challengePostId}")
    public ResponseEntity<ApiResponse<ChallengePostResponse>> getChallenge(@PathVariable("challengePostId") Long challengePostId) {
        ApiResponse<ChallengePostResponse> response = challengePostService.getChallengePost(challengePostId);
        return ResponseEntity.status(200).body(response);
    }
}
