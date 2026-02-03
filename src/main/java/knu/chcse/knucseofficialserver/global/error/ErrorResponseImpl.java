package knu.chcse.knucseofficialserver.global.error;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponseImpl implements ErrorResponse{

    private final String code;
    private final String message;

    @Builder
    public ErrorResponseImpl(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
