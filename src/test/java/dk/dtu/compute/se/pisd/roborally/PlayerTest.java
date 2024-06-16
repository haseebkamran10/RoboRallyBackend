package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.api.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlayerTest {

    private Board board;
    private Player player;

    @BeforeEach
    public void setUp() {
        board = new Board();
        player = new Player();
        player.setBoard(board);
        player.setSpace(board.getSpace(0, 0)); // Start position
    }

    @Test
    public void testPlayerMove() {
        player.setHeading(Heading.SOUTH);
        player.move(1);
        assertNotNull(player.getSpace());
        assertEquals(0, player.getSpace().getX());
        assertEquals(1, player.getSpace().getY());
    }

    @Test
    public void testPlayerEnergyChange() {
        int initialEnergy = player.getEnergy();
        player.changeEnergy(-10);
        assertEquals(initialEnergy - 10, player.getEnergy());
    }

    @Test
    public void testPlayerIndexIncrement() {
        int initialIndex = player.getIndex();
        player.incrementIndex();
        assertEquals(initialIndex + 1, player.getIndex());
    }

    @Test
    public void testPlayerHeadingChange() {
        player.setHeading(Heading.NORTH);
        assertEquals(Heading.NORTH, player.getHeading());
    }
}
