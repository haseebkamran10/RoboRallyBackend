package dk.dtu.compute.se.pisd.roborally.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents the game board.
 */
@Entity
@Getter
@Setter
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Space> spaces;


    public void addSpace(Space space) {
        spaces.add(space);
        space.setBoard(this);
    }

    // Utility method to remove a space from the board
    public void removeSpace(Space space) {
        spaces.remove(space);
        space.setBoard(null);
    }

    // Method to get a specific space by coordinates (assuming unique coordinates)
    public Space getSpace(int x, int y) {
        for (Space space : spaces) {
            if (space.getX() == x && space.getY() == y) {
                return space;
            }
        }
        return null;
    }
}
