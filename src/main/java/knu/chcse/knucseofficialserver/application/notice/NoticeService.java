package knu.chcse.knucseofficialserver.application.notice;

import jakarta.transaction.Transactional;
import knu.chcse.knucseofficialserver.application.notice.dto.CreateNoticeRequest;
import knu.chcse.knucseofficialserver.application.notice.dto.NoticeResponse;
import knu.chcse.knucseofficialserver.application.notice.dto.UpdateNoticeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

//interface 설계
public interface NoticeService {
    Long createNotice(CreateNoticeRequest request, Long studentNumber);
    NoticeResponse getNotice(Long noticeId);
    List<NoticeResponse> getNotices();
    void updateNotice(Long noticeId, Long studentNumber,UpdateNoticeRequest request);
    void deleteNotice(Long noticeId, Long studentNumber);

}
