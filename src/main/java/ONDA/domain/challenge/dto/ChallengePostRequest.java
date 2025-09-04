package ONDA.domain.challenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ChallengePostRequest {
    @Schema(description = "챌린지 ID", example = "1")
    private Long challengeId;

    @Schema(description = "이미지", example = "[\"https://example.com/image1.png\",\"https://example.com/image2.png\"]")
    private List<String> images;
}
