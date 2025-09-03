package ONDA.domain.challenge.controller;

import ONDA.domain.challenge.dto.ChallengeRequestDto;
import ONDA.domain.challenge.service.inf.ChallengeService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {
    private final ChallengeService challengeService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<ChallengeRequestDto>>> getAllChallenges(/*@AuthenticationPrincipal CustomUserDetails userDetails*/
                                                                        //RequestParam -> 임시
                                                                        @RequestParam Long memberId) {

        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS, challengeService.getAllChallenges(memberId)));
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Void>> createChallenge(@RequestBody ChallengeRequestDto dto,
                                                             /*@AuthenticationPrincipal CustomUserDetails userDetails*/
                                                             //RequestParam -> 임시
                                                             @RequestParam Long memberId) {
        challengeService.saveChallenge(/*userDetails.getMember().getId()*/memberId, dto);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.CREATED, null));
    }
}
