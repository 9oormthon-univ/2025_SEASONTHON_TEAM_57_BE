package ONDA.domain.talent.post.dto;

import ONDA.domain.talent.post.entity.PostStatus;
import ONDA.domain.talent.post.entity.PostType;
import ONDA.domain.talent.post.entity.TalentPost;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@Schema(description = "재능공유 게시글 응답")
public class TalentPostResponse {
    
    @Schema(description = "게시글 ID", example = "1")
    private Long id;
    
    @Schema(description = "작성자 ID", example = "1")
    private Long authorId;
    
    @Schema(description = "작성자 이름", example = "홍길동")
    private String authorName;
    
    @Schema(description = "게시글 타입", example = "learn/teach/trade")
    private PostType type;
    
    @Schema(description = "게시글 제목", example = "Java 프로그래밍 과외 해드립니다")
    private String title;
    
    @Schema(description = "게시글 내용", example = "5년차 개발자가 Java 기초부터 실무까지 알려드립니다.")
    private String content;
    
    @Schema(description = "생성일시", example = "2025-09-05T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "게시글 상태", example = "open/close")
    private PostStatus status;
    
    @Schema(description = "댓글 수", example = "5")
    private int commentCount;
    
    @Schema(description = "카테고리 목록")
    private List<CategoryResponse> categories;
    
    @Getter
    @Builder
    public static class CategoryResponse {
        @Schema(description = "카테고리 ID", example = "1")
        private Long id;
        
        @Schema(description = "카테고리 이름", example = "프로그래밍")
        private String name;
    }
    
    public static TalentPostResponse from(TalentPost post) {
        return TalentPostResponse.builder()
                .id(post.getId())
                .authorId(post.getAuthor().getId())
                .authorName(post.getAuthor().getNickname())
                .type(post.getType())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .status(post.getStatus())
                .commentCount(post.getComments().size())
                .categories(post.getCategories().stream()
                        .map(pc -> CategoryResponse.builder()
                                .id(pc.getCategory().getId())
                                .name(pc.getCategory().getName())
                                .build())
                        .toList())
                .build();
    }
}
