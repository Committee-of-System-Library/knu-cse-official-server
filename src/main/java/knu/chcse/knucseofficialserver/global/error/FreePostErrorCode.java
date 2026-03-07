package knu.chcse.knucseofficialserver.global.error;

import org.springframework.http.HttpStatus;

public enum FreePostErrorCode implements ErrorCode {

    NOT_FREE_POST(HttpStatus.BAD_REQUEST, "FP001", "자유게시판 글이 아닙니다."),
    NO_FREE_POST_PERMISSION(HttpStatus.FORBIDDEN, "FP002", "본인의 게시글만 수정/삭제할 수 있습니다."),
    CANNOT_MODIFY_COMMENTED_QUESTION(HttpStatus.CONFLICT, "FP003", "댓글이 달린 질문글은 수정/삭제할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    FreePostErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    @Override
    public HttpStatus HttpStatus() {
        return httpStatus;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}