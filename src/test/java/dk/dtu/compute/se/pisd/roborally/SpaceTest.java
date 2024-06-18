package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class SpaceTest {

    private Board board;
    private ActionField actionField;

    @BeforeEach
    void setUp() {
        board = new Board();
        actionField = new ActionField("TestField", ActionField.ActionType.CONVEYOR_BELT);
    }

    @Test
    void testSpaceConstructor() {
        Space space = new Space(1, 1, actionField, board);
        assertEquals(1, space.getX());
        assertEquals(1, space.getY());
        assertEquals(actionField, space.getActionField());
        assertEquals(board, space.getBoard());
    }

    @Test
    void testActivate() {
        Space space = new Space(1, 1, actionField, board);
        Player player = new Player();
        player.setSpace(space);
        player.setHeading(Heading.NORTH);

        space.activate(player);

        assertEquals(Heading.NORTH, player.getHeading());
        // Add more assertions based on the behavior of `activate` method
    }
}