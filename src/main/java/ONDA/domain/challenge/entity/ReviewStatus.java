package ONDA.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum ReviewStatus {
    @JsonProperty("pending")
    PENDING,

    @JsonProperty("approved")
    APPROVED,

    @JsonProperty("rejected")
    REJECTED
}
