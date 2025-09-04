package ONDA.domain.talent.post.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostStatus {
    OPEN("모집중"),CLOSE("모집완료");

    private final String displayName;
}
