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
}
