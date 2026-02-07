package knu.chcse.knucseofficialserver.global.error;

import org.springframework.http.HttpStatus;

public enum NoticeErrorCode implements ErrorCode {

    NOT_NOTICE(HttpStatus.BAD_REQUEST,"N001", "공지사항이 아닙니다."),
    NO_NOTICE_PERMISSION(HttpStatus.UNAUTHORIZED,"N002", "공지사항 등록 권한이 없습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    NoticeErrorCode(HttpStatus httpstatus,String code, String message) {
        this.httpStatus = httpstatus;
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
