package knu.chcse.knucseofficialserver.global.error;

public enum NoticeErrorCode implements ErrorCode {

    NOT_NOTICE("N001", "공지사항이 아닙니다."),
    NO_NOTICE_PERMISSION("N002", "공지사항 등록 권한이 없습니다.");


    private final String code;
    private final String message;

    NoticeErrorCode(String code, String message) {
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
