package knu.chcse.knucseofficialserver.application.notice.dto;

import java.util.List;

public record NoticeSyncRequest (
        List<NoticeItemRequest> data
){

}
