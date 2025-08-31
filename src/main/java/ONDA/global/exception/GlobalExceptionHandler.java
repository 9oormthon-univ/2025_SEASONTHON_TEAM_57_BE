package ONDA.global.exception;

import ONDA.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException e){
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity.
                status(ec.getStatus())
                .body(ApiResponse.error(ec));
    }
}
