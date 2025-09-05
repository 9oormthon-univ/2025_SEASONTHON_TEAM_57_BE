package ONDA.domain.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VoteResultResponse {
    @Id
    @GeneratedValue
    @Column(name = "participant_id", unique = true, nullable = false, updatable = false)
    @Schema(description = "참여자 ID", example = "1")
    private Long participantId;

    @Schema(description = "참여자 닉네임", example = "홍길동")
    private String participantNickname;

    @Schema(description = "참여자 프로필", example = "https://example.com/profile.png")
    private String participantProfile;

    @Schema(description = "투표 수", example = "100")
    private Long voteCount;

    @Schema(description = "순위", example = "1")
    private int rank;
}
