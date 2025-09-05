package ONDA.domain.talent.comment.controller;

import ONDA.domain.talent.comment.dto.CommentCreateRequest;
import ONDA.domain.talent.comment.dto.CommentResponse;
import ONDA.domain.talent.comment.dto.CommentUpdateRequest;
import ONDA.domain.talent.comment.service.CommentService;
import ONDA.global.response.ApiResponse;
import ONDA.global.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/talent-posts/{postId}/comments")
    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(
            @PathVariable("postId") Long postId,
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody CommentCreateRequest request) {
        CommentResponse response = commentService.create(postId, memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(ResponseCode.SUCCESS,response));
    }

    @GetMapping("/talent-posts/{postId}/comments")
    @Operation(summary = "게시글 댓글 목록 조회", description = "특정 게시글의 댓글들을 조회합니다")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getCommentsByPost(
            @PathVariable("postId") Long postId) {
        List<CommentResponse> response = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS,response));
    }


    @PutMapping("/comments/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody CommentUpdateRequest request) {
        CommentResponse response = commentService.update(commentId, memberId, request);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS,response));
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable("commentId") Long commentId,
            @AuthenticationPrincipal Long memberId) {
        commentService.delete(commentId, memberId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS,null));
    }


    @GetMapping("/comments/my")
    @Operation(summary = "내가 작성한 댓글 목록", description = "내가 작성한 댓글들을 조회합니다")
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getMyComments(
            @AuthenticationPrincipal Long memberId) {
        List<CommentResponse> response = commentService.getMyComments(memberId);
        return ResponseEntity.ok(ApiResponse.success(ResponseCode.SUCCESS,response));
    }
}
