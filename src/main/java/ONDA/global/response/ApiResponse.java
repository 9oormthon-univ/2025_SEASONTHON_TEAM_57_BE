package ONDA.global.response;

import ONDA.global.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Slf4j
@Getter
public class ApiResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(ResponseCode rc, T data) {
        return new ApiResponse<>(true, rc.getCode(), rc.getMessage(), data);
    }

    public static ApiResponse<Void> error(ErrorCode ec) {
        return new ApiResponse<>(false, ec.getCode(), ec.getMessage(), null);
    }
}
