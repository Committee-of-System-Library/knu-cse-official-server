package knu.chcse.knucseofficialserver.global.exception;

import knu.chcse.knucseofficialserver.global.error.ErrorCode;
import knu.chcse.knucseofficialserver.global.error.ErrorResponse;
import knu.chcse.knucseofficialserver.global.error.ErrorResponseImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomRuntimeException(BusinessException ex){

        ErrorCode errorCode = ex.getErrorCode();

         return ResponseEntity
                 .status(errorCode.HttpStatus())
                 .body(ErrorResponseImpl.builder()
                         .code(errorCode.code())
                         .message(errorCode.message())
                         .build());
    }
}
