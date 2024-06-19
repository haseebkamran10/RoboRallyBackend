package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.model.Heading;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private SpaceRepository spaceRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMovePlayer_Success() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");

        Space space = new Space();
        space.setId(1L);
        space.setX(2);
        space.setY(3);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(spaceRepository.findByXAndY(2, 3)).thenReturn(space);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player result = playerService.movePlayer(1L, 2, 3);

        assertNotNull(result);
        assertEquals(space, result.getSpace());
        verify(playerRepository, times(1)).findById(1L);
        verify(spaceRepository, times(1)).findByXAndY(2, 3);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void testMovePlayer_PlayerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        Player result = playerService.movePlayer(1L, 2, 3);

        assertNull(result);
        verify(playerRepository, times(1)).findById(1L);
        verify(spaceRepository, times(0)).findByXAndY(anyInt(), anyInt());
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    void testMovePlayer_SpaceNotFound() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(spaceRepository.findByXAndY(2, 3)).thenReturn(null);

        Player result = playerService.movePlayer(1L, 2, 3);

        assertNull(result);
        verify(playerRepository, times(1)).findById(1L);
        verify(spaceRepository, times(1)).findByXAndY(2, 3);
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    void testChangePlayerDirection_Success() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setHeading(Heading.NORTH);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player result = playerService.changePlayerDirection(1L, "EAST");

        assertNotNull(result);
        assertEquals(Heading.EAST, result.getHeading());
        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void testChangePlayerDirection_InvalidDirection() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            playerService.changePlayerDirection(1L, "INVALID");
        });

        String expectedMessage = "Invalid direction: INVALID";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    void testChangePlayerDirection_PlayerNotFound() {
        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        Player result = playerService.changePlayerDirection(1L, "EAST");

        assertNull(result);
        verify(playerRepository, times(1)).findById(1L);
        verify(playerRepository, times(0)).save(any(Player.class));
    }

    @Test
    void testJumpPlayer_Success() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setEnergy(100);  // Ensure player has enough energy

        Space currentSpace = new Space();
        currentSpace.setX(0);
        currentSpace.setY(0);
        currentSpace.setJumpPad(true);
        currentSpace.setObstacle(false);  // Current space is not an obstacle

        Space targetSpace = new Space();
        targetSpace.setX(1);
        targetSpace.setY(1);
        targetSpace.setObstacle(true);  // Target space is an obstacle

        player.setSpace(currentSpace);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(spaceRepository.findByXAndY(1, 1)).thenReturn(targetSpace);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        Player result = playerService.jumpPlayer(1L, 1, 1);

        assertNotNull(result, "The result should not be null");
        assertEquals(targetSpace, result.getSpace(), "The player should be at the target space");
        assertEquals(90, result.getEnergy(), "The player's energy should be 90 after jumping");  // Assuming jumping costs 10 energy

        verify(playerRepository, times(1)).save(player);
    }

    @Test
    void testMultipleJumps() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setEnergy(100);  // Reset energy

        Space currentSpace = new Space();
        currentSpace.setX(0);
        currentSpace.setY(0);
        currentSpace.setJumpPad(true);
        currentSpace.setObstacle(false);  // Current space is not an obstacle

        Space targetSpace1 = new Space();
        targetSpace1.setX(1);
        targetSpace1.setY(1);
        targetSpace1.setJumpPad(true);  // First target space is also a jump pad
        targetSpace1.setObstacle(true);  // First target space is an obstacle

        Space targetSpace2 = new Space();
        targetSpace2.setX(2);
        targetSpace2.setY(2);
        targetSpace2.setJumpPad(true);  // Second target space is also a jump pad
        targetSpace2.setObstacle(true);  // Second target space is an obstacle

        player.setSpace(currentSpace);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(spaceRepository.findByXAndY(1, 1)).thenReturn(targetSpace1);
        when(spaceRepository.findByXAndY(2, 2)).thenReturn(targetSpace2);
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        // First jump
        Player result1 = playerService.jumpPlayer(1L, 1, 1);
        assertNotNull(result1, "The result of the first jump should not be null");
        assertEquals(targetSpace1, result1.getSpace(), "The player should be at the first target space");
        assertEquals(90, result1.getEnergy(), "The player's energy should be 90 after the first jump");

        // Second jump
        Player result2 = playerService.jumpPlayer(1L, 2, 2);
        assertNotNull(result2, "The result of the second jump should not be null");
        assertEquals(targetSpace2, result2.getSpace(), "The player should be at the second target space");
        assertEquals(80, result2.getEnergy(), "The player's energy should be 80 after the second jump");

        verify(playerRepository, times(2)).save(player);
    }


    @Test
    void testJumpPlayer_Failure_InvalidTargetSpace() {
        Player player = new Player();
        player.setId(1L);
        player.setName("Player1");
        player.setEnergy(100);

        Space currentSpace = new Space();
        currentSpace.setX(0);
        currentSpace.setY(0);
        currentSpace.setJumpPad(true);

        player.setSpace(currentSpace);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));
        when(spaceRepository.findByXAndY(1, 1)).thenReturn(null);

        Player result = playerService.jumpPlayer(1L, 1, 1);

        assertNull(result);
        assertEquals(currentSpace, player.getSpace());
        assertEquals(100, player.getEnergy());
        verify(playerRepository, never()).save(player);
    }
}
