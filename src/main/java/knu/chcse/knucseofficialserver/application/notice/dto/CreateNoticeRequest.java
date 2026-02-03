package knu.chcse.knucseofficialserver.application.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public record CreateNoticeRequest (
    String title,
    String content
){}
