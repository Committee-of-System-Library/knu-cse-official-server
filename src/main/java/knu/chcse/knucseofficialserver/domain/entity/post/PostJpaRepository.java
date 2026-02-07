package knu.chcse.knucseofficialserver.domain.entity.post;

import knu.chcse.knucseofficialserver.domain.entity.board.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post,Long> {
    List<Post> findByBoard_CategoryAndStatusOrderByCreatedAtDesc(
            BoardCategory category,
            PostStatus status
    );
}
