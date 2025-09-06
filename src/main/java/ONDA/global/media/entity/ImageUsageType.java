package ONDA.global.media.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum ImageUsageType {
    TALENT_POST_IMAGE,
    PROFILE_IMAGE,
    CHALLENGE_IMAGE,
    CHALLENGE_POST_IMAGE;
}
