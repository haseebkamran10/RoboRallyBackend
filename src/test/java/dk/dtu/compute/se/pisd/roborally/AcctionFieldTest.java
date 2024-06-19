package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.api.model.ActionField;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ActionFieldTest {

    @Test
    void testActionFieldConstructor() {
        ActionField actionField = new ActionField("TestField", ActionField.ActionType.ICE_TILE);
        assertEquals("TestField", actionField.getName());
        assertTrue(actionField.isIceTile());
        assertFalse(actionField.isJumpPad());
        assertFalse(actionField.isObstacle());
    }

    @Test
    void testDefaultConstructor() {
        ActionField actionField = new ActionField();
        assertNotNull(actionField);
    }
}