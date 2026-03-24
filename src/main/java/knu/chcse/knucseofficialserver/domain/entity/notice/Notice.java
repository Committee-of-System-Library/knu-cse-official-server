package knu.chcse.knucseofficialserver.domain.entity.notice;

import jakarta.persistence.*;
import knu.chcse.knucseofficialserver.application.notice.dto.NoticeItemRequest;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "NOTICE_TABLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @Lob
    private String content;

    @Column(name = "created_at")
    private String createdAt;

    private String link;

    private Long num;

    @Column(name = "saved_at")
    private LocalDateTime savedAt;

    private String title;

    private String status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Notice from(NoticeItemRequest item){
        Notice notice = new Notice();
        notice.category = item.category();
        notice.content = item.content();
        notice.createdAt = item.createdAt();
        notice.link = item.link();
        notice.num = item.num();
        notice.savedAt = LocalDateTime.now();
        notice.title = item.title();
        notice.status = item.status();
        notice.updatedAt = LocalDateTime.now();
        return notice;
    }
    private Notice(
            String category,
            String content,
            String createdAt,
            String link,
            Long num,
            LocalDateTime savedAt,
            String title,
            String status,
            LocalDateTime updatedAt
    ) {
        this.category = category;
        this.content = content;
        this.createdAt = createdAt;
        this.link = link;
        this.num = num;
        this.savedAt = savedAt;
        this.title = title;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    public static Notice create(
            String category,
            String content,
            String createdAt,
            String link,
            Long num,
            LocalDateTime savedAt,
            String title,
            String status,
            LocalDateTime updatedAt
    ) {
        return new Notice(
                category,
                content,
                createdAt,
                link,
                num,
                savedAt,
                title,
                status,
                updatedAt
        );
    }
}