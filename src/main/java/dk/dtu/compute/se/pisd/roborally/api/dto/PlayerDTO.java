package dk.dtu.compute.se.pisd.roborally.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerDTO {
    private Long id;
    private String name;
    private String avatar;
    private Long gameSessionId;
    private boolean ready;

}
