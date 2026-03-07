package knu.chcse.knucseofficialserver.application.freepost.dto;

import knu.chcse.knucseofficialserver.domain.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FreePostResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private Long viewCount;
    private boolean question;
    private LocalDateTime pinnedUntil;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FreePostResponse from(Post post) {
        String author = post.isAnonymous() ? "익명" : post.getStudent().getNickname();

        return new FreePostResponse(
            post.getId(),
            post.getTitle(),
            post.getContent(),
            author,
            post.getViewCount(),
            post.isQuestion(),
            post.getPinnedUntil(),
            post.getCreatedAt(),
            post.getUpdatedAt()
        );
    }
}