package dk.dtu.compute.se.pisd.roborally.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameSessionDTO {
    private Long id;
    private Long boardId;
    private List<PlayerDTO> players;
    private int numberOfPlayers;
    private String joinCode;
    private Long hostId;
}
