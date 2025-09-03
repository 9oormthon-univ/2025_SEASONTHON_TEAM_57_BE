package ONDA.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ProgressStatus {
    @JsonProperty("not_started")
    NOT_STARTED("진행 예정"),

    @JsonProperty("ongoing")
    ONGOING("진행 중"),

    @JsonProperty("ended")
    ENDED("진행 완료");

    private final String displayName;

    ProgressStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    @Override
    public String toString() {
        return displayName;
    }
}
