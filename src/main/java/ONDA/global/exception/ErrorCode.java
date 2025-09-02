package ONDA.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST("E400", "잘못된 요청"),
    INTERNAL_ERROR("E500", "서버 오류가 발생"),

    // 인증/인가
    INVALID_TOKEN("AUTH001", "유효하지 않은 토큰"),
    EXPIRED_TOKEN("AUTH002", "만료된 토큰"),
    ACCESS_DENIED("AUTH003", "접근 권한이 없음"),
    MISSING_TOKEN("AUTH004", "토큰이 없음"),
    SIGNUP_SESSION_EXPIRED("AUTH005", "회원가입 링크 만료"),
    KAKAO_INVALID_TOKEN("OAUTH_KAKAO_400", "인가 코드 오류/만료"),
    KAKAO_INTERNAL_ERROR("OAUTH_KAKAO_500", "카카오 서버 오류"),

    //Validation
    INVALID_INPUT_VALUE("V001", "요청 값 검증에 실패");

    //도메인 에러 코드 작성

    private final String code;
    private final String message;
}
