package ONDA.domain.reputation.dto;

import ONDA.domain.member.entity.Member;
import ONDA.domain.reputation.entity.Reputation;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReputationResponse {
    @Schema(description = "사용자 닉네임", example = "홍길동")
    private String name;

    @Schema(description = "사용자 프로필", example = "https://example.com/image.png")
    private String profile;

    @Schema(description = "카테고리별 명성 점수 리스트")
    private List<CategoryScore> scoreByCategory;

    @Schema(description = "총 명성 점수", example = "100")
    private int totalScore;

    @Data
    @Builder
    @AllArgsConstructor
    public static class CategoryScore {
        @Schema(description = "카테고리 이름", example = "자기계발")
        private String categoryName;

        @Schema(description = "카테고리별 점수", example = "20")
        private int score;
    }

    public ReputationResponse(Member member, List<CategoryScore> scoreByCategory, int totalScore) {
        this.name = member.getNickname();
        this.profile = member.getProfile();
        this.scoreByCategory = scoreByCategory;
        this.totalScore = totalScore;
    }
}
