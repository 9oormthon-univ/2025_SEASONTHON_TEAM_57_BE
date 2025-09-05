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
    CHALLENGE_ALREADY_REVIEWED("CHALLENGE001", "이미 심사 완료된 챌린지는 상태 변경이 불가합니다."),
    ALREADY_VOTED("VOTE001", "이미 투표하였습니다."),


    OWNER_MISMATCH("OWNER_MISMATCH","삭제 권한 없음(해당 글의 작성자가 아님)."),
    POST_NOT_FOUND("POST-404", "게시글을 찾을 수 없음"),

    COMMENT_NOT_FOUND("COMMENT-404", "댓글을 찾을 수 없음"),
    PARENT_NOT_FOUND("PARENT-404", "존재하지 않는 부모 댓글입니다.(대댓글 전용)"),
    COMMENT_POST_MISMATCH("COMMENT_MISMATCH", "대댓글을 달기 위한 댓글이 해당글에 소속되어있지 않음"),

    // 이미지 업로드 관련
    EMPTY_FILE("FILE001", "빈 파일입니다"),
    FILE_SIZE_EXCEEDED("FILE002", "파일 크기가 제한을 초과했습니다 (최대 10MB)"),
    INVALID_FILE_TYPE("FILE003", "지원하지 않는 파일 형식입니다"),
    INVALID_FILE_NAME("FILE004", "잘못된 파일명입니다"),
    FILE_SAVE_FAILED("FILE005", "파일 저장에 실패했습니다"),
    IMAGE_NOT_FOUND("FILE006", "이미지를 찾을 수 없습니다"),
    IMAGE_READ_FAILED("FILE007", "이미지 읽기에 실패했습니다"),
    TOO_MANY_FILES("FILE008", "업로드 가능한 파일 수를 초과했습니다 (최대 5개)"),
    FORBIDDEN("FILE009", "권한이 없습니다");

    private final String code;
    private final String message;
}
