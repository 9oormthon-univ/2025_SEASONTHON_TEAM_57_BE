package ONDA.domain.challenge.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum ReviewStatus {
    @JsonProperty("pending")
    PENDING("보류 중"),

    @JsonProperty("approved")
    APPROVED("승인됨"),

    @JsonProperty("rejected")
    REJECTED("거절됨");

    private final String displayName;

    ReviewStatus(String displayName) {
        this.displayName = displayName;
    }

    @JsonValue
    @Override
    public String toString() { return displayName; }
}
