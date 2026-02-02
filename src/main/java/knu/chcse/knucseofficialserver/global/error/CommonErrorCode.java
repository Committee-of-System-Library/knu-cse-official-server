package knu.chcse.knucseofficialserver.global.error;

public enum CommonErrorCode implements ErrorCode {
    INVALID_REQUEST("C001", "잘못된 요청입니다."),
    NOT_FOUND("C002", "데이터를 찾을 수 없습니다."),
    UNAUTHORIZED("C003", "권한이 없습니다.");

    private final String code;
    private final String message;

    CommonErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
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
