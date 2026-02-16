package knu.chcse.knucseofficialserver.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ValidationErrorResponse implements ErrorResponse {

    private final String code;
    private final String message;
    private final List<FieldError> errors;

    @Builder
    public ValidationErrorResponse(String code, String message, List<FieldError> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
    }

    // 정적 팩토리 메서드
    public static ValidationErrorResponse of(String code, String message, BindingResult bindingResult) {
        return ValidationErrorResponse.builder()
            .code(code)
            .message(message)
            .errors(FieldError.of(bindingResult))
            .build();
    }

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String reason;

        // 정적 팩토리 메서드
        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors()
                .stream()
                .map(error -> FieldError.builder()
                    .field(error.getField())
                    .reason(error.getDefaultMessage())
                    .build())
                .collect(Collectors.toList());
        }
    }
}