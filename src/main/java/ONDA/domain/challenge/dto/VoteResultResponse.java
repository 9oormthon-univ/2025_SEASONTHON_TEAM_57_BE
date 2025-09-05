package ONDA.domain.challenge.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    private Long participantId;

    private String participantNickname;

    private Long voteCount;

    private int rank;
}
