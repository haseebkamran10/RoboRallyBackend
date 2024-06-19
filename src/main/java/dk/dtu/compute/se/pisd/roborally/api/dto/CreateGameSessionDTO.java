package dk.dtu.compute.se.pisd.roborally.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGameSessionDTO {
    private Long playerId;
    private Long boardId;
}
