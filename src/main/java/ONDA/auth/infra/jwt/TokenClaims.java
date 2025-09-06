package ONDA.auth.infra.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class TokenClaims {

    private final Long userId;          // sub
    private final String roles;          // roles
    private final String type;          // access | refresh
    private final String jti;           // 토큰 고유 ID

    public static TokenClaims of(Long userId,
                                 String roles,
                                 String type,
                                 String jti) {
        return new TokenClaims(userId, roles, type, jti);
    }

    public boolean isAccess() {
        return "access".equalsIgnoreCase(type);
    }

    public boolean isRefresh() {
        return "refresh".equalsIgnoreCase(type);
    }
}
