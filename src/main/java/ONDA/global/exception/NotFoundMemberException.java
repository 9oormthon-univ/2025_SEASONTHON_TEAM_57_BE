package ONDA.global.exception;

public class NotFoundMemberException extends BusinessException {
    public NotFoundMemberException() {
        super(ErrorCode.NOT_MEMBER_FOUND);
    }
}