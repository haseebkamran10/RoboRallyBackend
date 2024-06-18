package dk.dtu.compute.se.pisd.roborally.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    public boolean jump(Space targetSpace) {
        if (space.isJumpPad() && targetSpace.isObstacle() && consumeEnergy(10)) { // Assuming jumping costs 10 energy
            setSpace(targetSpace);
            return true;
        }
        return false;
    }

    // Method to handle sliding on ice tiles
    public boolean slide(Heading direction) {
        if (space.isIceTile()) {
            Space nextSpace = getNextSpaceInDirection(space, direction);
            while (nextSpace != null && nextSpace.isIceTile() && nextSpace.isObstacle()) {
                setSpace(nextSpace);
                nextSpace = getNextSpaceInDirection(nextSpace, direction);
            }
            return true;
        }
        return false;
    }

    // Utility method to get the next space in a given direction
    public Space getNextSpaceInDirection(Space currentSpace, Heading direction) {
        int nextX = currentSpace.getX();
        int nextY = currentSpace.getY();

        switch (direction) {
            case NORTH:
                nextY--;
                break;
            case EAST:
                nextX++;
                break;
            case SOUTH:
                nextY++;
                break;
            case WEST:
                nextX--;
                break;
        }

        // Assuming we have a method to get the space based on coordinates
        return gameSession.getBoard().getSpace(nextX, nextY);
    }

    // Method to move the player in a given direction with consideration for advanced mechanics
    public boolean move(Heading direction) {
        Space targetSpace = getNextSpaceInDirection(space, direction);

        if (targetSpace != null) {
            // Handle jumping
            if (space.isJumpPad() && jump(targetSpace)) {
                return true;
            }

            // Handle sliding
            if (space.isIceTile() && slide(direction)) {
                return true;
            }

            // Normal movement
            if (targetSpace.isObstacle() && consumeEnergy(1)) { // Assuming moving one space costs 1 energy
                setSpace(targetSpace);
                return true;
            }
        }
        return false;
    }

    // Setter for space with additional logic if needed
    public void setSpace(Space newSpace) {
        this.space = newSpace;
    }
}

// Method to handle jumping over obstacles

