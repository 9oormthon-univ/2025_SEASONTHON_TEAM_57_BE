package ONDA.global.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "C000", "성공"),
    CREATED(201, "C001", "생성 성공");

    private final int status;
    private final String code;
    private final String message;
}
