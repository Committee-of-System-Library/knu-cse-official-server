package knu.chcse.knucseofficialserver.application.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record UpdateNoticeRequest (
    String title,
    String content
){}
