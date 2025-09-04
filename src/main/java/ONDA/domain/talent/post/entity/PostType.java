package ONDA.domain.talent.post.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PostType {
    TEACH("알려주고 싶어요"),LEARN("배우고 싶어요"),TRADE("교환하고 싶어요");

    private final String displayName;
}
