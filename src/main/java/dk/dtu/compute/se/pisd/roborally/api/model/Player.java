package dk.dtu.compute.se.pisd.roborally.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String avatar;

    @ManyToOne
    @JsonBackReference
    private GameSession gameSession;

    @ManyToOne
    private Space space;

    @Enumerated(EnumType.STRING)
    private Heading heading;

    private int energy = 100; // Default energy level

    // Method to gain energy
    public void gainEnergy(int amount) {
        this.energy += amount;
    }

    // Method to consume energy
    public boolean consumeEnergy(int amount) {
        if (this.energy >= amount) {
            this.energy -= amount;
            return true;
        }
        return false;
    }
}