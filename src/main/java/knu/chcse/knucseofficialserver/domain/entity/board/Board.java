package knu.chcse.knucseofficialserver.domain.entity.board;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="board")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private BoardCategory category;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "write_permission_level", nullable = false)
    private WritePermissionLevel writePermissionLevel;

    public static Board create(
            BoardCategory category,
            String name,
            WritePermissionLevel writePermissionLevel
    ) {
        Board board = new Board();
        board.category = category;
        board.name = name;
        board.writePermissionLevel = writePermissionLevel;
        return board;
    }
}
