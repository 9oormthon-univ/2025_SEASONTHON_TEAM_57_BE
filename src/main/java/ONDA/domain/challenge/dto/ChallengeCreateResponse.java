package ONDA.domain.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ChallengeCreateResponse {
    @Schema(description = "챌린지 ID", example = "1")
    private Long challengeId;
}
