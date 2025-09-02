package ONDA.global.response;

import ONDA.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
@Getter
public class ApiResponse<T> {
    private int status;
    private String code;
    private String message;
    private T data;

    public ApiResponse(int status, String code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(ResponseCode rc, T data) {
        return new ApiResponse<>(rc.getStatus(), rc.getCode(), rc.getMessage(), data);
    }

    public static ApiResponse<Void> error(ErrorCode ec) {
        return new ApiResponse<>(ec.getStatus(), ec.getCode(), ec.getMessage(), null);
    }
}
