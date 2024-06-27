package dk.dtu.compute.se.pisd.roborally.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpaceDTO {
    private int x;
    private int y;
    private String type;
    private String heading;
    private int index;
}
