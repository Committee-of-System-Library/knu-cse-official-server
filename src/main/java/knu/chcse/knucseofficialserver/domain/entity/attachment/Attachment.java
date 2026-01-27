package knu.chcse.knucseofficialserver.domain.entity.attachment;

import jakarta.persistence.*;
import knu.chcse.knucseofficialserver.domain.entity.post.Post;
import lombok.*;

@Entity
@Table(name="attachment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "original_name", nullable = false)
    private String originalName;

    @Column(name = "save_name", nullable = false)
    private String saveName;


    @Column(name = "file_path", nullable = false)
    private String filePath;


    @Column(name = "file_size", nullable = false)
    private Long fileSize;


    @Column(name = "type", nullable = false)
    private String type;

    public static Attachment create(
            Post post,
            String originalName,
            String saveName,
            String filePath,
            Long fileSize,
            String type
    ){
        Attachment attachment = new Attachment();
        attachment.post = post;
        attachment.originalName = originalName;
        attachment.saveName = saveName;
        attachment.filePath = filePath;
        attachment.fileSize = fileSize;
        attachment.type = type;
        return attachment;
    }
}
