package knu.chcse.knucseofficialserver.domain.entity.post;

import jakarta.persistence.*;
import knu.chcse.knucseofficialserver.domain.entity.common.BaseTimeEntity;
import knu.chcse.knucseofficialserver.domain.entity.board.Board;
import knu.chcse.knucseofficialserver.domain.entity.student.Student;
import lombok.*;

@Entity
@Table(name="post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Post extends BaseTimeEntity {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "post_title",nullable = false)
    private String title;

    @Lob
    @Column(name = "post_content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",nullable = false)
    private Board board;

    @Column(name = "is_anonymous",nullable = false)
    private boolean isAnonymous;

    @Column(name = "view_count",nullable = false)
    private Long viewCount;

    @Column(name = "is_notice",nullable = false)
    private boolean isNotice;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false)
    private PostStatus status;

    //static factory method
    public static Post create(
            Student student,
            Board board,
            String title,
            String content,
            boolean isAnonymous){
        Post post = new Post();
        post.student = student;
        post.board = board;
        post.title = title;
        post.content = content;
        post.isAnonymous = isAnonymous;
        post.viewCount = 0L;
        post.isNotice = false;
        post.status = PostStatus.ACTIVE;
        return post;
    }
}
