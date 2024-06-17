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
}