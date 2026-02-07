package knu.chcse.knucseofficialserver.global.exception;

import knu.chcse.knucseofficialserver.global.error.ErrorResponse;
import knu.chcse.knucseofficialserver.global.error.ErrorResponseImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleCustomRuntimeException(BusinessException ex){
         ErrorResponse response = ErrorResponseImpl.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .build();

         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
