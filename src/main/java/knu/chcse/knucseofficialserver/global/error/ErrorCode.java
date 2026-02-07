package knu.chcse.knucseofficialserver.global.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    HttpStatus HttpStatus();
    String code();
    String message();
}
