package ONDA.domain.talent.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "댓글 생성 요청")
public class CommentCreateRequest {
    
    @NotBlank(message = "댓글 내용은 필수입니다")
    @Schema(description = "댓글 내용", example = "좋은 게시글이네요!")
    private String content;
    
    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "1")
    private Long parentId;
}
