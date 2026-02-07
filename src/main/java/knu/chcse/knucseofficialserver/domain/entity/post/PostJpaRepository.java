package knu.chcse.knucseofficialserver.domain.entity.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostJpaRepository extends JpaRepository<Post,Long> {
    List<Post> findByNoticeTrueAndStatusOrderByCreatedAtDesc(PostStatus status);
}
