package dk.dtu.compute.se.pisd.roborally.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ActionField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Enum representing the type of action field
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private boolean isIceTile;
    private boolean isJumpPad;
    private boolean isObstacle;

    private String name;

    public enum ActionType {
        CONVEYOR_BELT,
        RIGHT_CONVEYOR_BELT,
        LEFT_CONVEYOR_BELT,
        DOUBLE_CONVEYOR_BELT,
        DOUBLE_RIGHT_CONVEYOR_BELT,
        DOUBLE_LEFT_CONVEYOR_BELT,
        DOUBLE_LEFTTREE_CONVEYOR_BELT,
        DOUBLE_RIGHTTREE_CONVEYOR_BELT,
        LEFT_GEAR,
        RIGHT_GEAR,
        BOARD_LASER_START,
        BOARD_LASER,
        BOARD_LASER_END,
        PIT,
        PUSH_PANEL,
        STARTING_GEAR,
        ENERGY_SPACE,
        CHECKPOINT,
        WALL,
        PRIORITY_ANTENNA,
        RESPAWN,
        NORMAL,
        ICE_TILE, // New type for ice tiles
        JUMP_PAD, // New type for jump pads
        OBSTACLE  // New type for obstacles
    }

    // Constructor to initialize the action field with a specific type
    public ActionField(String name, ActionType actionType) {
        this.name = name;
        this.actionType = actionType;
        this.isIceTile = actionType == ActionType.ICE_TILE;
        this.isJumpPad = actionType == ActionType.JUMP_PAD;
        this.isObstacle = actionType == ActionType.OBSTACLE;
    }

    // Default constructor for JPA
    public ActionField() {}
}
