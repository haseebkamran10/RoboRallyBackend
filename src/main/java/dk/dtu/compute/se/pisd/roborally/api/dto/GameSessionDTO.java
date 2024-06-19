package dk.dtu.compute.se.pisd.roborally.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameSessionDTO {
    private Long id;
    private Long boardId;
    private Long hostId;
    private String joinCode;
    private int numberOfPlayers;

}
