package ONDA.domain.talent.post.dto;

import ONDA.domain.talent.post.entity.PostStatus;
import ONDA.domain.talent.post.entity.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "재능공유 게시글 수정 요청")
public class TalentPostUpdateRequest {
    
    @Schema(description = "게시글 타입", example = "teach")
    private PostType type;
    
    @Schema(description = "게시글 제목", example = "Java 프로그래밍 과외 해드립니다 (수정)")
    private String title;
    
    @Schema(description = "게시글 내용", example = "5년차 개발자가 Java 기초부터 실무까지 알려드립니다. (내용 수정)")
    private String content;
    
    @Schema(description = "게시글 상태", example = "open/close")
    private PostStatus status;
    
    @Schema(description = "배우고 싶은 카테고리 ID 목록", example = "[1, 2]")
    private List<Long> learnCategoryIds;
    
    @Schema(description = "가르치고 싶은 카테고리 ID 목록", example = "[3, 4]")
    private List<Long> teachCategoryIds;

    @Schema(description = "가격", example = "10000")
    private Integer price;
}
