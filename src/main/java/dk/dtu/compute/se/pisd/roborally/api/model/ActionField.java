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

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    private String name;

    public enum ActionType {
        CONVEYOR_BELT(false, false, false),
        RIGHT_CONVEYOR_BELT(false, false, false),
        LEFT_CONVEYOR_BELT(false, false, false),
        DOUBLE_CONVEYOR_BELT(false, false, false),
        DOUBLE_RIGHT_CONVEYOR_BELT(false, false, false),
        DOUBLE_LEFT_CONVEYOR_BELT(false, false, false),
        DOUBLE_LEFTTREE_CONVEYOR_BELT(false, false, false),
        DOUBLE_RIGHTTREE_CONVEYOR_BELT(false, false, false),
        LEFT_GEAR(false, false, false),
        RIGHT_GEAR(false, false, false),
        BOARD_LASER_START(false, false, false),
        BOARD_LASER(false, false, false),
        BOARD_LASER_END(false, false, false),
        PIT(false, false, false),
        PUSH_PANEL(false, false, false),
        STARTING_GEAR(false, false, false),
        ENERGY_SPACE(false, false, false),
        CHECKPOINT(false, false, false),
        WALL(false, false, false),
        PRIORITY_ANTENNA(false, false, false),
        RESPAWN(false, false, false),
        NORMAL(false, false, false),
        ICE_TILE(true, false, false),
        JUMP_PAD(false, true, false),
        OBSTACLE(false, false, true);

        private final boolean isIceTile;
        private final boolean isJumpPad;
        private final boolean isObstacle;

        ActionType(boolean isIceTile, boolean isJumpPad, boolean isObstacle) {
            this.isIceTile = isIceTile;
            this.isJumpPad = isJumpPad;
            this.isObstacle = isObstacle;
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
    }

    public ActionField(String name, ActionType actionType) {
        this.name = name;
        this.actionType = actionType;
    }

    public ActionField() {}
}
