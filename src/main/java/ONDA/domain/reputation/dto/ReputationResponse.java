package ONDA.domain.reputation.dto;

import ONDA.domain.reputation.entity.Reputation;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReputationResponse {
    @Schema(description = "사용자 닉네임", example = "홍길동")
    private String name;

    @Schema(description = "카테고리 이름", example = "자기계발")
    private String categoryName;

    @Schema(description = "명성 점수", example = "123")
    private int score;

    public ReputationResponse(Reputation reputation) {
        this.name = reputation.getMember().getNickname();
        this.categoryName = reputation.getCategory().getName();
        this.score = reputation.getScore();
    }
}
