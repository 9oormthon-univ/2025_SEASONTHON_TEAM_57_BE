package ONDA.global.config;

import ONDA.auth.infra.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    @Order(1) //permit
    SecurityFilterChain publicChain(HttpSecurity http) throws Exception {
        http.securityMatcher(
                "/api/v1/test/**",
                "/api/v1/auth/**",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/api/media/images/**"
        );
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
        );
        return http.build();
    }

    @Bean
    @Order(2) //authenticated
    SecurityFilterChain protectedChain(HttpSecurity http, AuthenticationEntryPoint entryPoint,
                                       AccessDeniedHandler deniedHandler) throws Exception {
        http.securityMatcher("/**");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //인증 필요 목록
                        .requestMatchers(HttpMethod.GET, "/api/talent-posts/my").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/talent-posts/recommended").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/challenges/review").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/challenges/my*").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/profile/**").authenticated()

                        .requestMatchers(HttpMethod.GET, "/api/challenges/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/talent-posts/**").permitAll()

                        .anyRequest().authenticated())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(deniedHandler)
                );
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(allowedOrigins));
        config.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization","Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
