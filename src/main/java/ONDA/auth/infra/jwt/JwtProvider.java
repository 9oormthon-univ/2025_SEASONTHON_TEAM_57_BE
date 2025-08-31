package ONDA.auth.infra.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtProvider {

    private final Key key;

    @Value("${jwt.access-exp-seconds}")
    private long ACCESS_TOKEN_EXPIRE_TIME;

    @Value("${jwt.refresh-exp-seconds}")
    private long REFRESH_TOKEN_EXPIRE_TIME;

    public JwtProvider(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String createAccessToken(Long memberId) {
        return createToken(memberId, "access", ACCESS_TOKEN_EXPIRE_TIME);
    }

    public String createRefreshToken(Long memberId) {
        return createToken(memberId, "refresh", REFRESH_TOKEN_EXPIRE_TIME);
    }

    private String createToken(Long memberId, String type, long expireTime) {

        return Jwts.builder()
                .setSubject(type)
                .setId(UUID.randomUUID().toString())
                .claim("memberId", memberId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            throw new CustomException(AuthErrorCode.TOKEN_EXPIRED);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            throw new CustomException(AuthErrorCode.MALFORMED_TOKEN);
        }catch (Exception e) {
            throw new CustomException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public long getRemainingMillis(Claims claims) {
        Date expiration = claims.getExpiration(); // 만료 시간
        long now = System.currentTimeMillis();    // 현재 시간

        return Math.max(expiration.getTime() - now, 0);
    }

    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }

    public String resolveToken(String header) {
        return (header != null && header.startsWith("Bearer ")) ? header.substring(7) : null;
    }
}



