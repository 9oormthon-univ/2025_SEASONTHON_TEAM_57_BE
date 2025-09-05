package ONDA.domain.talent.post.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {
    @JsonProperty("teach")
    TEACH("알려주고 싶어요"),

    @JsonProperty("learn")
    LEARN("배우고 싶어요"),

    @JsonProperty("trade")
    TRADE("교환하고 싶어요");

    private final String displayName;
}
