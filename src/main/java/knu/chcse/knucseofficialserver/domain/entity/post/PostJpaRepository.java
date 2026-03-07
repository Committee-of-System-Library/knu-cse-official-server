package knu.chcse.knucseofficialserver.domain.entity.post;

import knu.chcse.knucseofficialserver.domain.entity.board.BoardCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post, Long> {

    List<Post> findByBoard_CategoryAndStatusOrderByCreatedAtDesc(
        BoardCategory category,
        PostStatus status
    );

    // 자유게시판 - 현재 고정 중인 질문글 최신순 최대 5개
    // question = true AND pinnedUntil > now
    List<Post> findTop5ByBoard_CategoryAndStatusAndQuestionTrueAndPinnedUntilAfterOrderByCreatedAtDesc(
        BoardCategory category,
        PostStatus status,
        LocalDateTime now
    );

    // 자유게시판 - 전체 게시글 최신순 페이징
    Page<Post> findByBoard_CategoryAndStatusOrderByCreatedAtDesc(
        BoardCategory category,
        PostStatus status,
        Pageable pageable
    );
}