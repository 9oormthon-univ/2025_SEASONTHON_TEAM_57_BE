package ONDA.auth.infra.jwt;

import ONDA.auth.infra.redis.RedisService;
import ONDA.domain.member.entity.Role;
import ONDA.global.exception.ErrorCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private final Key key;

    private final RedisService redisService;


    @Value("${jwt.access-exp-seconds}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh-exp-seconds}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtProvider(@Value("${jwt.secret}") String secret, RedisService redisService) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.redisService = redisService;
    }

    public String generateAccessToken(Long userId, Role roles) {
        return buildToken(userId, TokenType.ACCESS, roles, ACCESS_TOKEN_EXPIRE_TIME, true);
    }

    public String generateRefreshToken(Long userId) {
        return buildToken(userId, TokenType.REFRESH, null, REFRESH_TOKEN_EXPIRE_TIME, false);
    }

    private String buildToken(Long memberId, TokenType type, Role roles, long expireTime, boolean includeRoles) {

        JwtBuilder builder = Jwts.builder()
                .setSubject(memberId.toString())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .claim("type", type.name().toLowerCase());

        if (includeRoles && roles != null) builder.claim("roles", roles);

        return builder.signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public TokenClaims parseAndValidate(String token, String expectedType) throws AuthenticationException {
        Claims c = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();


        // type 일치 여부
        String type = String.valueOf(c.get("type"));
        if (expectedType != null && (!expectedType.equalsIgnoreCase(type))) {
            //throw new ErrorCode.JwtAuthException(ErrorCode.ACCESS_DENIED);
        }

        String jti = c.getId();

        // 블랙리스트 여부
        if (redisService.exists("bl:access:" + jti)) {
            //throw new CustomException(AuthErrorCode.TOKEN_BLACKLISTED);
        }

        Long userId = Long.valueOf(c.getSubject());
        String roles = String.valueOf(c.get("roles"));


        return TokenClaims.of(userId, roles, type, jti);
    }

    public String resolveToken(String bearer) {
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }
}



