package dk.dtu.compute.se.pisd.roborally.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Space {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;

    @Enumerated(EnumType.STRING)
    private ActionField type;

    @ManyToOne
    @JoinColumn(name = "board_id")
    @JsonBackReference
    private Board board;

    public Space() {
        // Default constructor for JPA
    }

    public Space(int x, int y, Board board, ActionField type) {
        this.x = x;
        this.y = y;
        this.board = board;
        this.type = type;
    }
}
