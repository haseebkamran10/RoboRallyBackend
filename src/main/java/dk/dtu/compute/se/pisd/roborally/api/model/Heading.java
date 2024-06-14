package dk.dtu.compute.se.pisd.roborally.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents the heading of a robot.
 */
@Entity
@Getter
@Setter
public class Heading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
