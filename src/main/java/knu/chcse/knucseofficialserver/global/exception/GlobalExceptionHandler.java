package knu.chcse.knucseofficialserver.global.exception;

import knu.chcse.knucseofficialserver.global.error.ErrorCode;
import knu.chcse.knucseofficialserver.global.error.ErrorResponse;
import knu.chcse.knucseofficialserver.global.error.ErrorResponseImpl;
import knu.chcse.knucseofficialserver.global.error.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

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

        BindingResult bindingResult = ex.getBindingResult();

        List<ValidationErrorResponse.FieldError> fieldErrors = bindingResult.getFieldErrors()
            .stream()
            .map(error -> ValidationErrorResponse.FieldError.builder()
                .field(error.getField())
                .value(error.getRejectedValue() != null
                    ? error.getRejectedValue().toString()
                    : "null")
                .reason(error.getDefaultMessage())
                .build())
            .collect(Collectors.toList());

        ValidationErrorResponse errorResponse = ValidationErrorResponse.builder()
            .code("V001")
            .message("입력값이 올바르지 않습니다.")
            .errors(fieldErrors)
            .build();

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(errorResponse);
    }
}
