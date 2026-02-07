package knu.chcse.knucseofficialserver.domain.entity.board;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface BoardJpaRepository extends JpaRepository<Board,Long> {

    Optional<Board> findByCategory(BoardCategory category);
}
