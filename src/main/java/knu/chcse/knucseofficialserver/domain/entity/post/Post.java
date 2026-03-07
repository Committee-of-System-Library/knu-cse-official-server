package knu.chcse.knucseofficialserver.domain.entity.post;

import jakarta.persistence.*;
import knu.chcse.knucseofficialserver.domain.entity.common.BaseTimeEntity;
import knu.chcse.knucseofficialserver.domain.entity.board.Board;
import knu.chcse.knucseofficialserver.domain.entity.board.BoardCategory;
import knu.chcse.knucseofficialserver.domain.entity.student.Student;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name="post")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post extends BaseTimeEntity {

    private static final int QUESTION_PIN_DAYS = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private boolean anonymous;

    @Column(name = "view_count",nullable = false)
    private Long viewCount;

    @Column(name = "is_pinned",nullable = false)
    private boolean pinned;

    @Column(name = "pinned_until")
    private LocalDateTime pinnedUntil;

    @Column(name = "question", nullable = false)
    private boolean question;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false)
    private PostStatus status;

    //static factory method
    public static Post create(
        Student student,
        Board board,
        String title,
        String content,
        boolean anonymous,
        boolean question
    ) {
        Post post = new Post();
        post.student = student;
        post.board = board;
        post.title = title;
        post.content = content;
        post.anonymous = anonymous;
        post.question = question;
        post.viewCount = 0L;
        post.pinned = false;
        post.pinnedUntil = question ? LocalDateTime.now().plusDays(QUESTION_PIN_DAYS) : null;
        post.status = PostStatus.ACTIVE;
        return post;
    }

    public boolean isNotice(){
        return this.board.getCategory() == BoardCategory.NOTICE;
    }

    public boolean isFreePost() { return this.board.getCategory() == BoardCategory.FREE; }

    public boolean isOwnedBy(Long studentNumber) {
        return this.student.getNumber().equals(studentNumber);
    }

    //질문글에 댓글이 달린 경우 수정/삭제 불가
    public boolean isModifiable(boolean hasComments) {
        return !(question && hasComments);
    }

    //domain 중심 설계
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void delete(){
        this.status = PostStatus.DELETED;
    }

    // 상단 고정 토글 메서드 (추후 구현용)
    public void togglePin(){
        this.pinned = !this.pinned;
    }

    // 조회수 증가 메서드
    public void incrementViewCount(){
        this.viewCount++;
    }
}