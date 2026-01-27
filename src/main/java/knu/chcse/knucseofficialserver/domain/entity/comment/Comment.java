package knu.chcse.knucseofficialserver.domain.entity.comment;

import jakarta.persistence.*;
import knu.chcse.knucseofficialserver.domain.entity.common.BaseTimeEntity;
import knu.chcse.knucseofficialserver.domain.entity.post.Post;
import knu.chcse.knucseofficialserver.domain.entity.student.Student;
import lombok.*;

@Entity
@Table(name="comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends BaseTimeEntity {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Lob
    @Column(name = "comment_content", nullable = false)
    private String content;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public static Comment create(
        Post post,
        Student student,
        String content
    ){
        Comment comment = new Comment();
        comment.post = post;
        comment.student = student;
        comment.content = content;
        comment.isDeleted = false;
        return comment;
    }
}
