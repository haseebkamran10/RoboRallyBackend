package dk.dtu.compute.se.pisd.roborally.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BoardDTO {
    private Long id;
    private String name;
    private int width = 13; // Default value
    private int height = 10; // Default value
    private List<List<SpaceDTO>> spaces; // Ensure this field is present but can be null
}
