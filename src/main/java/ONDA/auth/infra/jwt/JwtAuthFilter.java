package ONDA.auth.infra.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AntPathMatcher m = new AntPathMatcher();

    private final JwtProvider jwtProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        boolean isGet = "GET".equalsIgnoreCase(method);

        return isGet && (
                !m.match("/api/talent-posts/my", uri) &&
                        !m.match("/api/talent-posts/recommended", uri) &&
                        !m.match("/api/challenges/review", uri) &&
                        !m.match("/api/challenges/my*", uri)) &&
                        !m.match("/api/challenge-posts/my*", uri) &&
                        !m.match("/api/profile/**", uri);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException, AuthenticationException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                TokenClaims claims = jwtProvider.parseAndValidate(token, "access");

                Authentication auth = createAuthentication(claims);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (ExpiredJwtException e) {
                //throw new JwtAuthenticationException("토큰 만료", e);
                throw new IllegalArgumentException();

            } catch (JwtException e) {
                //throw new JwtAuthenticationException("유효하지 않은 토큰", e);
            }
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest req) {
        String bearer = req.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer ")) ? bearer.substring(7) : null;
    }

    private Authentication createAuthentication(TokenClaims claims) {
        Collection<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(claims.getRoles()));
        return new UsernamePasswordAuthenticationToken(
                claims.getUserId(), null, authorities
        );
    }

    @Configuration
    public static class FilterConfig {
        @Bean
        public FilterRegistrationBean<JwtAuthFilter> jwtAuthFilterRegistration(JwtAuthFilter jwtAuthFilter) {
            FilterRegistrationBean<JwtAuthFilter> registration = new FilterRegistrationBean<>(jwtAuthFilter);
            registration.setEnabled(false); //필터 활성화
            return registration;
        }
    }
}