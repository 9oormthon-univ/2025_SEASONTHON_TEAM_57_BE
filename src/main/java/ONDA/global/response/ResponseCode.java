package ONDA.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS("C000", "성공"),
    CREATED( "C001", "생성 성공"),

    AUTH_LOGIN_SUCCESS("A001","로그인 성공"),
    AUTH_SIGNUP_REQUIRED("A002","회원가입 필요"),

    TALENT_POST_CREATE_SUCCESS("T001", "재능공유 게시글이 성공적으로 작성되었습니다."),
    TALENT_POST_READ_SUCCESS("T002", "재능공유 게시글 조회가 완료되었습니다."),
    TALENT_POST_UPDATE_SUCCESS("T003", "재능공유 게시글이 성공적으로 수정되었습니다."),
    TALENT_POST_DELETE_SUCCESS("T004", "재능공유 게시글이 성공적으로 삭제되었습니다."),
    TALENT_POST_LIST_SUCCESS("T005", "재능공유 게시글 목록 조회가 완료되었습니다.");

    private final String code;
    private final String message;
}
