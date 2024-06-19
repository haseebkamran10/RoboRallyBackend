package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setEnergy(100); // setting default energy
    }

    @Test
    void testGainEnergy() {
        player.gainEnergy(25);
        assertEquals(125, player.getEnergy());
    }

    @Test
    void testConsumeEnergy_Success() {
        boolean result = player.consumeEnergy(30);
        assertTrue(result);
        assertEquals(70, player.getEnergy());
    }

    @Test
    void testConsumeEnergy_InsufficientEnergy() {
        boolean result = player.consumeEnergy(150);
        assertFalse(result);
        assertEquals(100, player.getEnergy());
    }
}