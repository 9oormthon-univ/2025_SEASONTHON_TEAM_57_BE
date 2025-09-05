package ONDA.domain.talent.post.controller;

import ONDA.domain.talent.post.dto.TalentPostCreateRequest;
import ONDA.domain.talent.post.dto.TalentPostListResponse;
import ONDA.domain.talent.post.dto.TalentPostResponse;
import ONDA.domain.talent.post.dto.TalentPostUpdateRequest;
import ONDA.domain.talent.post.service.TalentPostService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/talent-posts")
@RequiredArgsConstructor
@Tag(name = "TalentPost", description = "재능공유 게시글 API")
public class TalentPostController {

    private final TalentPostService talentPostService;

    @PostMapping
    @Operation(summary = "재능공유 게시글 작성", description = "재능 공유 게시글을 작성합니다")
    public ResponseEntity<ApiResponse<TalentPostResponse>> createPost(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody TalentPostCreateRequest request) {
        TalentPostResponse response = talentPostService.create(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ResponseCode.TALENT_POST_CREATE_SUCCESS,response));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "개별 게시글 조회", description = "ID로 게시글을 조회합니다")
    public ResponseEntity<ApiResponse<TalentPostResponse>> getPostById(
            @PathVariable("postId") Long postId) {
        TalentPostResponse response = talentPostService.getById(postId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.TALENT_POST_READ_SUCCESS,response));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "카테고리별 게시글 조회", description = "선택한 카테고리의 게시글들을 조회합니다")
    public ResponseEntity<ApiResponse<List<TalentPostListResponse>>> getPostByCategory(
            @PathVariable("categoryId") Long categoryId) {
        List<TalentPostListResponse> response = talentPostService.getByCategory(categoryId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.TALENT_POST_READ_SUCCESS,response));
    }

    @GetMapping("/hot")
    @Operation(summary = "Hot한 재능공유 조회", description = "현재 Hot한 재능들을 조회합니다")
    public ResponseEntity<ApiResponse<List<TalentPostListResponse>>> getHotPost() {
        List<TalentPostListResponse> response = talentPostService.getHotPost();
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.TALENT_POST_READ_SUCCESS,response));
    }

    @GetMapping("/recommended")
    @Operation(summary = "맞춤 재능공유 추천", description = "사용자에게 알맞는 재능들을 추천합니다")
    public ResponseEntity<ApiResponse<List<TalentPostListResponse>>> getRecommendedPost(
            @AuthenticationPrincipal Long memberId) {
        List<TalentPostListResponse> response = talentPostService.getRecommended(memberId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.TALENT_POST_READ_SUCCESS,response));
    }

    @GetMapping("/my")
    @Operation(summary = "내가 작성한 게시글 조회", description = "내가 작성한 재능공유 게시글들을 조회합니다")
    public ResponseEntity<ApiResponse<List<TalentPostListResponse>>> getMyPosts(
            @AuthenticationPrincipal Long memberId) {
        List<TalentPostListResponse> response = talentPostService.getMyPosts(memberId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.TALENT_POST_READ_SUCCESS,response));
    }

    @PutMapping("/{postId}")
    @Operation(summary = "재능공유 게시글 수정", description = "재능공유 글을 수정합니다")
    public ResponseEntity<ApiResponse<TalentPostResponse>> update(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody TalentPostUpdateRequest request) {
        TalentPostResponse response = talentPostService.update(postId, memberId, request);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.TALENT_POST_UPDATE_SUCCESS,response));
    }

    @DeleteMapping("/{postId}")
    @Operation(summary = "재능공유 게시글 삭제", description = "재능공유 글을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal Long memberId) {
        talentPostService.delete(postId, memberId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.TALENT_POST_DELETE_SUCCESS,null));
    }
}