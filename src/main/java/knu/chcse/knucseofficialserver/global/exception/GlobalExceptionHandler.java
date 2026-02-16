package knu.chcse.knucseofficialserver.global.exception;

import knu.chcse.knucseofficialserver.global.error.ErrorCode;
import knu.chcse.knucseofficialserver.global.error.ErrorResponse;
import knu.chcse.knucseofficialserver.global.error.ErrorResponseImpl;
import knu.chcse.knucseofficialserver.global.error.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {

        ValidationErrorResponse errorResponse = ValidationErrorResponse.of(
            "V001",
            "입력값이 올바르지 않습니다.",
            ex.getBindingResult()
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }
}