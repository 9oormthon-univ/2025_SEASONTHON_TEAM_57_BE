package ONDA.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS("C000", "성공"),
    CREATED( "C001", "생성 성공"),

    AUTH_LOGIN_SUCCESS("A001","로그인 성공"),
    AUTH_SIGNUP_REQUIRED("A002","회원가입 필요");

    private final String code;
    private final String message;
}
