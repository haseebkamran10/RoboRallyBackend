package dk.dtu.compute.se.pisd.roborally.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String avatar;
    private String color;

    @ManyToOne
    @JsonBackReference
    private GameSession gameSession;

    @OneToOne
    private Space space;

    @Enumerated(EnumType.STRING)
    private Heading heading = Heading.SOUTH;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandCardField> program = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandCardField> cards = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandCardField> upgrades = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommandCardField> upgradeInv = new ArrayList<>();

    private int energy = 100; // Default energy level
    private int index = 0;
    private double distance;

    @Enumerated(EnumType.STRING)
    private Phase phase;

    @Transient
    private Board board;

    // Movement-related methods
    public void incrementIndex() {
        this.index++;
    }

    public void move(int n) {
        if (n == 0) {
            return;
        } else {
            move(n - 1);
        }

        Space nextSpace = switch (heading) {
            case SOUTH -> board.getSpace(space.getX(), space.getY() + 1);
            case NORTH -> board.getSpace(space.getX(), space.getY() - 1);
            case WEST -> board.getSpace(space.getX() - 1, space.getY());
            case EAST -> board.getSpace(space.getX() + 1, space.getY());
        };

        if (nextSpace.getType() == ActionField.WALL)
            return;
        else if (nextSpace.getType() == ActionField.CHECKPOINT)
            incrementIndex();

        setSpace(nextSpace);
    }

    // Additional methods for energy
    public void changeEnergy(int amount) {
        this.energy += amount;
        this.energy = Math.max(0, this.energy); // Ensure energy does not go below zero
    }
}
