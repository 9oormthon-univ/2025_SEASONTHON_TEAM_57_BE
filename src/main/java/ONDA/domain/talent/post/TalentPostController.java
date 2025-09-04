package ONDA.domain.talent.post;

import ONDA.domain.talent.post.service.TalentPostService;
import ONDA.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/talent-posts")
@RequiredArgsConstructor
public class TalentPostController {
    private final TalentPostService talentPostService;

    @GetMapping
    @Operation(summary = "재능공유 게시글 작성", description = "재능 공유 게시글을 작성합니다")
    public ResponseEntity<ApiResponse<Void>> createPost(@AuthenticationPrincipal Long memberId) {
        talentPostService.create();
        return null;
    }

    @GetMapping("/{postId}")
    @Operation(summary = "개별 게시글 조회", description = "Id로 게시글을 조회합니다")
    public ResponseEntity<ApiResponse<Void>> getPostById(@PathVariable("postId") Long postId) {
        talentPostService.getById(postId);
        return null;
    }

    @GetMapping
    @Operation(summary = "카테고리 별로 조회", description = "선택한 카테고리의 게시글들을 조회합니다")
    public ResponseEntity<ApiResponse<Void>> getPostByCategory(@RequestParam("categoryId") Long categoryId) {
        talentPostService.getByCategory(categoryId);
        return null;
    }

    @GetMapping("/hot")
    @Operation(summary = "Hot한 재능공유 조회", description = "현재 Hot한 재능들을 조회합니다")
    public ResponseEntity<ApiResponse<Void>> getHotPost() {
        talentPostService.getHotPost();
        return null;
    }

    @Operation(summary = "맞춤 재능공유 추천", description = "사용자에게 알맞는 재능들을 추천합니다")
    public ResponseEntity<ApiResponse<Void>> getRecommendedPost(@AuthenticationPrincipal Long memberId) {
        talentPostService.getRecommended(memberId);
        return null;
    }

    @Operation(summary = " 재능공유 게시글 수정", description = "재능공유 글을 수정합니다")
    public ResponseEntity<ApiResponse<Void>> update() {
        talentPostService.update();
        return null;
    }

    @Operation(summary = " 재능공유 게시글 삭제", description = "재능공유 글을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deletePost() {
        talentPostService.delete();
        return null;
    }

}
