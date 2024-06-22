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

    // Constructor
    public Space(int x, int y, ActionField actionField, Board board) {
        this.x = x;
        this.y = y;
        this.actionField = actionField;
        this.board = board;
        updateAdvancedMovementMechanics();
    }

    // Default constructor for JPA
    public Space() {}

    // Method to update advanced movement mechanics based on ActionField
    @PostLoad
    private void updateAdvancedMovementMechanics() {
        if (actionField != null) {
            this.isIceTile = actionField.isIceTile();
            this.isJumpPad = actionField.isJumpPad();
            this.isObstacle = actionField.isObstacle();
        }
    }

    // Method to activate space effects
    public void activate(Player player) {
        if (player != null) {
            Heading oldHeading = player.getHeading();
            switch (actionField.getActionType()) {
                case LEFT_CONVEYOR_BELT:
                    player.setHeading(oldHeading.prev());
                    player.move(oldHeading.prev());
                    break;
                case RIGHT_CONVEYOR_BELT:
                    player.setHeading(oldHeading.next());
                    player.move(oldHeading.next());
                    break;
                case CONVEYOR_BELT:
                    player.move(oldHeading);
                    break;
                case DOUBLE_CONVEYOR_BELT:
                    player.move(oldHeading);
                    player.move(oldHeading);
                    break;
                case LEFT_GEAR:
                    player.setHeading(oldHeading.prev());
                    break;
                case RIGHT_GEAR:
                    player.setHeading(oldHeading.next());
                    break;
                case BOARD_LASER:
                    // Implement laser effect (e.g., reducing player's energy)
                    break;
                case PIT:
                    // Implement pit effect (e.g., resetting player's position)
                    break;
                case ENERGY_SPACE:
                    player.gainEnergy(10); // Example of gaining energy
                    break;
                case CHECKPOINT:
                    player.setLastCheckpoint(this);
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
        return isObstacle;
    }

    public boolean isFreeOfObstacles() {
        return !isObstacle;
    }
}
