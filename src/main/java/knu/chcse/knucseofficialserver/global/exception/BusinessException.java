package knu.chcse.knucseofficialserver.global.exception;

import knu.chcse.knucseofficialserver.global.error.ErrorCode;
import lombok.Getter;

import java.util.Objects;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        //super로 인한 npe 방지. super는 항상 첫줄에 써야하므로, Objects로 처리
        super(Objects.requireNonNull(errorCode, "errorCode must not be null").message());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode.code();
    }
}
