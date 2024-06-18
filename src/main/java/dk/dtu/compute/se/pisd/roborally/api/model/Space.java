package dk.dtu.compute.se.pisd.roborally.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Space {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int x;
    private int y;

    @ManyToOne
    @JoinColumn(name = "action_field_id")
    private ActionField actionField;

    // Reference back to Board
    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    // New fields to indicate specific advanced movement mechanics
    private boolean isIceTile;
    private boolean isJumpPad;
    private boolean isObstacle;

    // Method to update advanced movement mechanics based on ActionField
    @PostLoad
    private void updateAdvancedMovementMechanics() {
        if (actionField != null) {
            this.isIceTile = actionField.isIceTile();
            this.isJumpPad = actionField.isJumpPad();
            this.isObstacle = actionField.isObstacle();
        }
    }

    // Constructor
    public Space(int x, int y, ActionField actionField, Board board) {
        this.x = x;
        this.y = y;
        this.actionField = actionField;
        this.board = board;
    }

    // Default constructor for JPA
    public Space() {}

    // Method to activate space effects
    public void activate(Player player) {
        if (player != null) {
            Heading oldHeading = player.getHeading();
            switch (actionField.getName()) {
                case "LEFT_CONVEYOR_BELT":
                    player.setHeading(oldHeading.prev());
                    player.move(oldHeading.prev());
                    break;
                case "RIGHT_CONVEYOR_BELT":
                    player.setHeading(oldHeading.next());
                    player.move(oldHeading.next());
                    break;
                case "CONVEYOR_BELT":
                    player.move(oldHeading);
                    break;
                case "DOUBLE_CONVEYOR_BELT":
                    player.move(oldHeading);
                    player.move(oldHeading);
                    break;
                case "LEFT_GEAR":
                    player.setHeading(oldHeading.prev());
                    break;
                case "RIGHT_GEAR":
                    player.setHeading(oldHeading.next());
                    break;
                case "BOARD_LASER":
                    // Implement laser effect (e.g., reducing player's energy)
                    break;
                case "PIT":
                    // Implement pit effect (e.g., resetting player's position)
                    break;
                case "ENERGY_SPACE":
                    player.gainEnergy(10); // Example of gaining energy
                    break;
                default:
                    // No special action
                    break;
            }
        }
    }

    public boolean isIceTile() {
        return isIceTile;
    }

    public boolean isJumpPad() {
        return isJumpPad;
    }

    public boolean isObstacle() {
        return !isObstacle;
    }
}
