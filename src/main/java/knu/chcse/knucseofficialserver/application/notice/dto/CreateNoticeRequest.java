package knu.chcse.knucseofficialserver.application.notice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNoticeRequest {
    private String title;
    private String content;
}
