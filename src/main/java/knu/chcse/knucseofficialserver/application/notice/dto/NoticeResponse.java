package knu.chcse.knucseofficialserver.application.notice.dto;

import knu.chcse.knucseofficialserver.domain.entity.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeResponse {

    private Long id;
    private String title;
    private String content;


    public static NoticeResponse from(Post post){
        return new NoticeResponse(
                post.getId(),
                post.getTitle(),
                post.getContent());
    }
}
