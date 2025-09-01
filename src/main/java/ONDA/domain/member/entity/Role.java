package ONDA.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ROLE_USER("ROLE_USER", "일반 회원"),
    ROLE_ADMIN("ROLE_ADMIN", "관리자");

    private final String authority;
    private final String label;
}
