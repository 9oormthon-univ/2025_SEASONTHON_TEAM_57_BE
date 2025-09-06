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
@Schema(description = "재능공유 게시글 목록 응답")
public class TalentPostListResponse {
    
    @Schema(description = "게시글 ID", example = "1")
    private Long id;
    
    @Schema(description = "작성자 이름", example = "홍길동")
    private String authorName;
    
    @Schema(description = "게시글 타입", example = "teach/learn/trade")
    private PostType type;
    
    @Schema(description = "게시글 제목", example = "Java 프로그래밍 과외 해드립니다")
    private String title;
    
    @Schema(description = "작성일", example = "2025-09-04T10:30:00")
    private LocalDateTime createdAt;
    
    @Schema(description = "게시글 상태", example = "open/close")
    private PostStatus status;
    
    @Schema(description = "댓글 수", example = "5")
    private int commentCount;
    
    @Schema(description = "카테고리 목록")
    private List<String> categoryNames;
    
    public static TalentPostListResponse from(TalentPost post) {
        return TalentPostListResponse.builder()
                .id(post.getId())
                .authorName(post.getAuthor().getNickname())
                .type(post.getType())
                .title(post.getTitle())
                .createdAt(post.getCreatedAt())
                .status(post.getStatus())
                .commentCount(post.getComments().size())
                .categoryNames(post.getCategories().stream()
                        .map(pc -> pc.getCategory().getName())
                        .toList())
                .build();
    }
}
