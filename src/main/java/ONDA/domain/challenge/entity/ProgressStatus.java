package ONDA.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum ProgressStatus {
    @JsonProperty("not_started")
    NOT_STARTED,

    @JsonProperty("ongoing")
    ONGOING,

    @JsonProperty("ended")
    ENDED
}
