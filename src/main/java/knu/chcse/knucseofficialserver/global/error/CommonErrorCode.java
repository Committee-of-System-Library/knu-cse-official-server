package knu.chcse.knucseofficialserver.global.error;

import org.springframework.http.HttpStatus;

public enum CommonErrorCode implements ErrorCode {
    NOT_FOUND(HttpStatus.NOT_FOUND,"C002", "데이터를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"C003", "권한이 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED,"C004", "인증 정보가 올바르지 않습니다.(일치하는 학번이 없음)");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    CommonErrorCode(HttpStatus httpStatus,String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus HttpStatus() {
        return httpStatus;
    }


    @Override
    public String code(){
        return code;
    }

    @Override
    public String message(){
        return message;
    }


}
