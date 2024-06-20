package dk.dtu.compute.se.pisd.roborally.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Getter
@Setter
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Board board;

    @OneToMany(mappedBy = "gameSession")
    @JsonManagedReference
    private List<Player> players;

    private int numberOfPlayers;

    private String joinCode;

    @ManyToOne
    private Player host;

    private int maxPlayers;

    private boolean gameStarted;
}
