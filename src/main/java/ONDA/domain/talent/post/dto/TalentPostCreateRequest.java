package ONDA.domain.talent.post.dto;

import ONDA.domain.talent.post.entity.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "재능공유 게시글 생성 요청")
public class TalentPostCreateRequest {
    
    @NotNull(message = "게시글 타입은 필수입니다")
    @Schema(description = "게시글 타입", example = "teach/learn/trade")
    private PostType type;
    
    @NotBlank(message = "제목은 필수입니다")
    @Schema(description = "게시글 제목", example = "Java 프로그래밍 과외 해드립니다")
    private String title;
    
    @NotBlank(message = "내용은 필수입니다")
    @Schema(description = "게시글 내용", example = "5년차 개발자가 Java 기초부터 실무까지 알려드립니다.")
    private String content;
    
    @Schema(description = "배우고 싶은 카테고리 ID 목록", example = "[1, 2]")
    private List<Long> learnCategoryIds;
    
    @Schema(description = "알려주고 싶은 카테고리 ID 목록", example = "[3, 4]")
    private List<Long> teachCategoryIds;

    @Schema(description = "가격", example = "10000")
    private int price;
}
