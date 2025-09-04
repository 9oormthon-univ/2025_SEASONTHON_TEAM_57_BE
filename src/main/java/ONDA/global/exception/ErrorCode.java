package ONDA.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

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
    INVALID_INPUT_VALUE("V001", "요청 값 검증에 실패"),

    //도메인 에러 코드 작성
    NOT_MEMBER_FOUND("MEMBER-404", "회원을 찾을 수 없습니다."),
    NOT_CHALLENGE_FOUND("CHALLENGE-404", "챌린지를 찾을 수 없습니다."),
    NOT_CHALLENGE_POST_FOUND("CHALLENGE-POST-404", "챌린지 인증글을 찾을 수 없습니다."),
    CHALLENGE_ALREADY_REVIEWED("CHALLENGE001", "이미 심사 완료된 챌린지는 상태 변경이 불가합니다.");

    private final String code;
    private final String message;
}
