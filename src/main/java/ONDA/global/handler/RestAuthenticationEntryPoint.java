package ONDA.global.handler;

import ONDA.global.exception.ErrorCode;
import ONDA.global.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        // 예외 종류별로 ErrorCode 매핑
        ErrorCode errorCode = (ex.getCause() instanceof AuthenticationException)
                ? ErrorCode.INVALID_TOKEN
                : ErrorCode.BAD_REQUEST;


        ApiResponse<Void> body = ApiResponse.error(errorCode);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}