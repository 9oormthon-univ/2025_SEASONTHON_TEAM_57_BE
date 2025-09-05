package ONDA.domain.challenge.controller;

import ONDA.domain.challenge.dto.ChallengePostRequest;
import ONDA.domain.challenge.dto.ChallengePostResponse;
import ONDA.domain.challenge.dto.VoteResultResponse;
import ONDA.domain.challenge.service.inf.ChallengePostService;
import ONDA.domain.challenge.service.inf.ChallengeVoteService;
import ONDA.domain.member.dto.MemberResponse;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/challenge-posts")
@RequiredArgsConstructor
@Tag(name = "ChallengePost", description = "챌린지 인증글 API")
public class ChallengePostController {
    private final ChallengePostService challengePostService;
    private final ChallengeVoteService challengeVoteService;

    @Operation(summary = "챌린지 인증글 생성", description = "진행중인 챌린지 인증글 하나를 생성합니다")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "챌린지 인증글 생성 성공")
    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createChallengePost(@AuthenticationPrincipal Long memberId,
                                                             @RequestBody ChallengePostRequest dto) {
        challengePostService.saveChallengePost(memberId, dto);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }

    @Operation(summary = "챌린지 인증글 조회", description = "챌린지 인증글 하나를 조회합니다")
    @GetMapping("/{challengePostId}")
    public ResponseEntity<ApiResponse<ChallengePostResponse>> getChallengePost(@PathVariable("challengePostId") Long challengePostId) {
        ApiResponse<ChallengePostResponse> response = challengePostService.getChallengePost(challengePostId);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "내 챌린지 인증글 리스트 챌린지별 조회", description = "내 챌린지 인증글을 챌린지별 조회합니다")
    @GetMapping("/my/{challengeId}")
    public ResponseEntity<ApiResponse<List<ChallengePostResponse>>> getMyChallengePostsByChallenge(
            @AuthenticationPrincipal Long memberId,
            @PathVariable("challengeId") Long challengeId) {
        ApiResponse<List<ChallengePostResponse>> response = challengePostService.getMyChallengePostsByChallenge(memberId,challengeId);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "날짜별 내 챌린지 인증글 목록 조회", description = "날짜별 내 챌린지 인증글 목록을 조회합니다")
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ChallengePostResponse>>> getMyChallengePostsByChallenge(
            @AuthenticationPrincipal Long memberId,
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "month",required = false) Integer month,
            @RequestParam(name = "day",required = false) Integer day)
    {
        // 현재 날짜 기준으로 기본값 설정
        LocalDate now = LocalDate.now();
        int targetYear = (year == null) ? now.getYear() : year;
        int targetMonth = (month == null) ? now.getMonthValue() : month;
        int targetDay = (day == null) ? now.getDayOfMonth() : day;

        LocalDate targetDate = LocalDate.of(targetYear, targetMonth, targetDay);

        ApiResponse<List<ChallengePostResponse>> response = challengePostService.getMyChallengePostsByDate(memberId,targetDate);
        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "챌린지 참여자 목록 조회", description = "특정 챌린지에 인증글을 올린 참여자 목록을 조회합니다.")
    @GetMapping("/{challengeId}/participants")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getChallengeParticipants(
            @PathVariable("challengeId") Long challengeId) {
        return ResponseEntity.status(200).body(challengePostService.getChallengeParticipants(challengeId));
    }

    @Operation(summary = "특정 챌린지 참여자의 인증글 목록 조회",
            description = "challengeId와 memberId로 해당 챌린지에서 특정 사용자가 올린 인증글들을 조회합니다.")
    @GetMapping("/{challengeId}/posts/{memberId}")
    public ResponseEntity<ApiResponse<List<ChallengePostResponse>>> getMemberChallengePosts(
            @PathVariable("challengeId") Long challengeId,
            @PathVariable("memberId") Long memberId) {

        ApiResponse<List<ChallengePostResponse>> response =
                challengePostService.getPostsByChallengeAndMember(challengeId, memberId);

        return ResponseEntity.status(200).body(response);
    }

    @Operation(summary = "특정 챌린지 참여자 투표",
            description = "해당 챌린지에서 특정 참여자를 투표합니다.")
    @PostMapping("/{challengeId}/vote/{participantId}")
    public ResponseEntity<ApiResponse<Void>> voteForParticipant(@AuthenticationPrincipal Long voterId,
                                                @PathVariable("challengeId") Long challengeId,
                                                @PathVariable("participantId") Long participantId) {

        challengeVoteService.voteForParticipant(voterId, challengeId, participantId);
        return ResponseEntity.status(201).body(ApiResponse.success(ResponseCode.CREATED, null));
    }

    @Operation(summary = "챌린지 투표 결과 조회",
            description = "챌린지의 투표 결과를 조회합니다.")
    @GetMapping("/{challengeId}/votes")
    public ResponseEntity<ApiResponse<List<VoteResultResponse>>> getVoteResults(@PathVariable("challengeId") Long challengeId) {

        ApiResponse<List<VoteResultResponse>> response =
                challengeVoteService.getVoteResults(challengeId);

        return ResponseEntity.status(200).body(response);
    }

}
