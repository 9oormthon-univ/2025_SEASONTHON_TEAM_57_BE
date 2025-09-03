package ONDA.domain.challenge.controller;

import ONDA.domain.challenge.dto.ChallengeRequest;
import ONDA.domain.challenge.service.inf.ChallengeService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ChallengeRequest>>> getAllChallenges(/*@AuthenticationPrincipal CustomUserDetails userDetails*/) {

        Long memberId = 1L;
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS, challengeService.getAllChallenges(memberId)));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createChallenge(
                                                             /*@AuthenticationPrincipal CustomUserDetails userDetails*/
                                                             //RequestParam -> 임시
                                                             @RequestBody ChallengeRequest dto) {
        Long memberId = 1L;
        challengeService.saveChallenge(memberId, dto);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.CREATED, null));
    }
}
