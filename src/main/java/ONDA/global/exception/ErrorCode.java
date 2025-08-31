package ONDA.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(400, "E400", "잘못된 요청입니다"),
    INTERNAL_ERROR(500, "E500", "서버 오류가 발생했습니다"),

    // 인증/인가
    INVALID_TOKEN(401, "AUTH001", "유효하지 않은 토큰입니다"),
    ACCESS_DENIED(403, "AUTH002", "접근 권한이 없습니다");

    //도메인 에러 코드 작성

    private final int status;
    private final String code;
    private final String message;
}
