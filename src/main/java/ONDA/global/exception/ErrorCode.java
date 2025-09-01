package ONDA.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(400, "E400", "잘못된 요청"),
    INTERNAL_ERROR(500, "E500", "서버 오류가 발생"),

    // 인증/인가
    INVALID_TOKEN(401, "AUTH001", "유효하지 않은 토큰"),
    EXPIRED_TOKEN(401, "AUTH002", "만료된 토큰"),
    ACCESS_DENIED(403, "AUTH003", "접근 권한이 없음"),
    SIGNUP_SESSION_EXPIRED(401, "AUTH004", "회원가입 링크 만료"),
    KAKAO_INVALID_TOKEN(401, "OAUTH_KAKAO_400", "인가 코드 오류/만료"),
    KAKAO_INTERNAL_ERROR(500, "OAUTH_KAKAO_500", "카카오 서버 오류");


    //도메인 에러 코드 작성

    private final int status;
    private final String code;
    private final String message;
}
