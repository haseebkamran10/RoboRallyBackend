package dk.dtu.compute.se.pisd.roborally.api.dto;

import java.util.ArrayList;
import java.util.List;

public class BoardDTO {
    private Long id;
    private String name;
    private List<SpaceDTO> spaces = new ArrayList<>();

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SpaceDTO> getSpaces() {
        return spaces;
    }

    public void setSpaces(List<SpaceDTO> spaces) {
        this.spaces = spaces;
    }
}
