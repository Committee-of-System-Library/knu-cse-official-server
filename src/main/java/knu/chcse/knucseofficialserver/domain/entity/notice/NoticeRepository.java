package knu.chcse.knucseofficialserver.domain.entity.notice;

import knu.chcse.knucseofficialserver.domain.entity.notice.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,Long> {
    Optional<Notice> findByNum(Long num);

    Optional<Notice> findByLink(String link);

    boolean existsByNum(Long num);

    boolean existsByLink(String link);
}
