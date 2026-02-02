package knu.chcse.knucseofficialserver.global.exception;

import knu.chcse.knucseofficialserver.global.error.ErrorResponse;
import knu.chcse.knucseofficialserver.global.error.ErrorResponseImpl;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ErrorResponse handleCustomRuntimeException(BusinessException ex){
        return ErrorResponseImpl.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();
    }
}
