package ONDA.domain.challenge.controller;

import ONDA.domain.challenge.dto.ChallengeRequest;
import ONDA.domain.challenge.dto.ChallengeResponse;
import ONDA.domain.challenge.entity.ProgressStatus;
import ONDA.domain.challenge.entity.ReviewStatus;
import ONDA.domain.challenge.service.inf.ChallengeService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

//    @Operation(summary = "모든 챌린지 조회", description = "현재 생성된 모든 챌린지를 조회합니다")
//    @GetMapping("/all")
//    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getAllChallenges() {
//        ApiResponse<List<ChallengeResponse>> response = challengeService.getAllChallenges();
//        return ResponseEntity.status(200).body(response);
//    }

    @Operation(summary = "내가 등록한 챌린지 리스트 조회", description = "내가 등록한 모든 챌린지를 조회합니다")
    @GetMapping("/my-challenges")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getMyChallenges(@AuthenticationPrincipal Long memberId) {
        ApiResponse<List<ChallengeResponse>> response = challengeService.getMyChallenges(memberId);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "승인된 챌린지 리스트 카테고리별 조회", description = "심사 승인된 챌린지를 재능 카테고리별 조회합니다")
    @GetMapping("/approve-list")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getChallengesByCategory(
            @RequestParam("categoryId") Long categoryId) {
        ApiResponse<List<ChallengeResponse>> response = challengeService.getChallengesByCategory(categoryId);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "승인된 챌린지 리스트 전체 조회", description = "심사 승인된 챌린지를 전체 조회합니다")
    @GetMapping("/approve-list/all")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getAllChallenges() {
        ApiResponse<List<ChallengeResponse>> response = challengeService.getAllChallenges();
        return ResponseEntity.status(200).body(response);
    }

//    @Operation(summary = "승인된 챌린지 리스트 진행 카테고리별 조회", description = "심사 승인된 챌린지를 진행 카테고리별 조회합니다")
//    @GetMapping("/approve-list/progress")
//    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getChallengesByProgressStatus(
//            @Parameter(
//                    description = "진행 상태 (NOT_STARTED: 진행 예정, ONGOING: 진행 중, ENDED: 진행 완료)",
//                    example = "ONGOING"
//            )
//            @RequestParam("progressStatus") ProgressStatus progressStatus) {
//        ApiResponse<List<ChallengeResponse>> response = challengeService.getChallengesByProgressStatus(progressStatus);
//        return ResponseEntity.status(200).body(response);
//    }

    @Operation(summary = "챌린지 생성", description = "챌린지 하나를 생성합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "챌린지 생성 성공")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createChallenge(@AuthenticationPrincipal Long memberId,
                                                             @RequestBody ChallengeRequest dto) {
        challengeService.saveChallenge(memberId, dto);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }

    @Operation(summary = "챌린지 조회", description = "챌린지 하나를 조회합니다")
    @GetMapping("/{challengeId}")
    public ResponseEntity<ApiResponse<ChallengeResponse>> getChallenge(@PathVariable("challengeId") Long challengeId) {
        ApiResponse<ChallengeResponse> response = challengeService.getChallenge(challengeId);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "지금 HOT한 챌린지 조회", description = "참여자 수를 기반으로 챌린지 목록을 조회합니다")
    @GetMapping("/popular")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getPopularChallenges() {
        ApiResponse<List<ChallengeResponse>> response = challengeService.getOngoingChallengesOrderByParticipants();
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "내가 참여한 챌린지 리스트 조회", description = "내가 참여한 챌린지 리스트를 조회합니다")
    @GetMapping("/my-challenge-posts")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getMyChallengePosts(@AuthenticationPrincipal Long memberId) {
        ApiResponse<List<ChallengeResponse>> response = challengeService.getMyChallengePosts(memberId);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "챌린지 리스트 심사 카테고리별 조회", description = "심사 카테고리별 챌린지를 조회합니다")
    @GetMapping("/review")
    public ResponseEntity<ApiResponse<List<ChallengeResponse>>> getChallengesByReviewStatus(
            @Parameter(
                    description = "심사 상태 (PENDING: 보류, APPROVED: 승인, REJECTED: 거절)",
                    example = "PENDING"
            )
            @RequestParam("reviewStatus") ReviewStatus reviewStatus) {
        ApiResponse<List<ChallengeResponse>> response = challengeService.getChallengesByReviewStatus(reviewStatus);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "챌린지 심사 보류", description = "운영진이 해당 챌린지를 보류합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "챌린지 보류 성공")
    @PostMapping("/{challengeId}/review/pending")
    public ResponseEntity<ApiResponse<Void>> pendingChallenge(@PathVariable("challengeId") Long challengeId) {
        challengeService.pendingChallenge(challengeId);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }

    @Operation(summary = "챌린지 심사 승인", description = "운영진이 해당 챌린지를 승인합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "챌린지 승인 성공")
    @PostMapping("/{challengeId}/review/approve")
    public ResponseEntity<ApiResponse<Void>> approveChallenge(@PathVariable("challengeId") Long challengeId) {
        challengeService.approveChallenge(challengeId);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }

    @Operation(summary = "챌린지 심사 거절", description = "운영진이 해당 챌린지를 거절합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "챌린지 거절 성공")
    @PostMapping("/{challengeId}/review/reject")
    public ResponseEntity<ApiResponse<Void>> rejectChallenge(@PathVariable("challengeId") Long challengeId) {
        challengeService.rejectChallenge(challengeId);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }
}
