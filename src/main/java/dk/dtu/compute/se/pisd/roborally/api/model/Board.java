package dk.dtu.compute.se.pisd.roborally.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Space> spaces;

    public Board() {
        // Initialize the spaces list (for example, a 10x10 grid)
        initializeSpaces(10, 10);
    }

    private void initializeSpaces(int width, int height) {
        spaces = new ArrayList<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Initialize spaces with a default type, e.g., NORMAL
                Space space = new Space(x, y, this, ActionField.NORMAL);
                spaces.add(space);
            }
        }
    }

    public Space getSpace(int x, int y) {
        return spaces.stream()
                .filter(space -> space.getX() == x && space.getY() == y)
                .findFirst()
                .orElse(null);
    }
}
