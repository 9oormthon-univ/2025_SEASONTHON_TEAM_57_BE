package ONDA.domain.talent.post.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatus {
    @JsonProperty("open")
    OPEN("모집중"),

    @JsonProperty("close")
    CLOSE("모집완료");

    private final String displayName;
}
