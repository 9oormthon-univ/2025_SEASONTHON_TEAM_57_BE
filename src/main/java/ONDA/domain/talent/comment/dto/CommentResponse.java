package ONDA.domain.talent.comment.dto;

import ONDA.domain.talent.comment.entity.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "댓글 응답")
public class CommentResponse {
    
    @Schema(description = "댓글 ID", example = "1")
    private Long id;
    
    @Schema(description = "작성자 ID", example = "1")
    private Long authorId;
    
    @Schema(description = "작성자 이름", example = "홍길동")
    private String authorName;
    
    @Schema(description = "댓글 내용", example = "좋은 게시글이네요!")
    private String content;
    
    @Schema(description = "생성일시", example = "2025-01-15T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "1")
    private Long parentId;
    
    @Schema(description = "대댓글 목록")
    private List<CommentResponse> children;
    
    public static CommentResponse from(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .children(comment.getChildren().stream()
                        .map(CommentResponse::from)
                        .toList())
                .build();
    }
    
    public static CommentResponse fromWithoutChildren(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .parentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .children(null)
                .build();
    }
}
