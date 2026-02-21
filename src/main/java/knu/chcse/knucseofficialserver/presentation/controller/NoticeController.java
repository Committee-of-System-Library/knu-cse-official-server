package knu.chcse.knucseofficialserver.presentation.controller;

import jakarta.validation.Valid;
import knu.chcse.knucseofficialserver.application.notice.NoticeService;
import knu.chcse.knucseofficialserver.application.notice.dto.CreateNoticeRequest;
import knu.chcse.knucseofficialserver.application.notice.dto.NoticeResponse;
import knu.chcse.knucseofficialserver.application.notice.dto.UpdateNoticeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController implements NoticeControllerDocs {

    private final NoticeService noticeService;

    @PostMapping
    @Override
    public ResponseEntity<Long> createNotice(
        @RequestHeader("X-Student-Number") Long studentNumber,
        @Valid @RequestBody CreateNoticeRequest request
    ) {
        Long noticeId = noticeService.createNotice(request, studentNumber);

        URI location = URI.create("/api/notices/" + noticeId);

        return ResponseEntity
            .created(location)
            .body(noticeId);
    }

    @GetMapping("/{noticeId}")
    @Override
    public ResponseEntity<NoticeResponse> getNotice(
        @PathVariable Long noticeId
    ) {
        NoticeResponse response = noticeService.getNotice(noticeId);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Override
    public ResponseEntity<List<NoticeResponse>> getNotices() {
        List<NoticeResponse> responses = noticeService.getNotices();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{noticeId}")
    @Override
    public ResponseEntity<Void> updateNotice(
        @PathVariable Long noticeId,
        @RequestHeader("X-Student-Number") Long studentNumber,
        @Valid @RequestBody UpdateNoticeRequest request
    ) {
        noticeService.updateNotice(noticeId, studentNumber, request);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{noticeId}")
    @Override
    public ResponseEntity<Void> deleteNotice(
        @PathVariable Long noticeId,
        @RequestHeader("X-Student-Number") Long studentNumber
    ) {
        noticeService.deleteNotice(noticeId, studentNumber);

        return ResponseEntity.noContent().build();
    }
}