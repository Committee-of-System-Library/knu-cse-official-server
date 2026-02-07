package knu.chcse.knucseofficialserver.global.error;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse implements ErrorResponse {

    private final String code;
    private final String message;
    private final List<FieldError> errors;

    @Builder
    public ValidationErrorResponse(String code, String message, List<FieldError> errors) {
        this.code = code;
        this.message = message;
        this.errors = errors != null ? errors : new ArrayList<>();
    }

    @Getter
    @Builder
    public static class FieldError {
        private final String field;
        private final String value;
        private final String reason;
    }
}