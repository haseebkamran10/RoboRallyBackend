package dk.dtu.compute.se.pisd.roborally.api.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerDTO {
    private Long id; // Add this field
    private String name;
    private String avatar;

    // Default constructor
    public PlayerDTO() {
    }

    // Constructor with id, name, and avatar
    public PlayerDTO(Long id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    // Constructor without id
    public PlayerDTO(String name, String avatar) {
        this.name = name;
        this.avatar = avatar;
    }
}
