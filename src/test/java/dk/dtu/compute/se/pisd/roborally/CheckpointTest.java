package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.api.model.ActionField;
import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerService;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CheckpointTest {

    @Autowired
    private PlayerService playerService;

    @MockBean
    private PlayerRepository playerRepository;

    @MockBean
    private SpaceRepository spaceRepository;

    private Player player;
    private Board board;

    @BeforeEach
    public void setUp() {
        // Create a mock player
        player = new Player();
        player.setId(1L); // Set an ID to ensure the player can be retrieved later
        player.setName("Test Player");

        // Create a mock board
        board = new Board();
        board.setId(1L);

        // Save the mock player to the repository
        Mockito.when(playerRepository.save(Mockito.any(Player.class))).thenReturn(player);
        Mockito.when(playerRepository.findById(player.getId())).thenReturn(Optional.of(player));

        // Mock the space repository
        Mockito.when(spaceRepository.findByXAndY(Mockito.anyInt(), Mockito.anyInt()))
                .thenAnswer(invocation -> {
                    int x = invocation.getArgument(0);
                    int y = invocation.getArgument(1);
                    Space space = new Space(x, y, null, board);
                    if (x == 2 && y == 2) {
                        ActionField checkpointField = new ActionField("CHECKPOINT", ActionField.ActionType.CHECKPOINT);
                        space.setActionField(checkpointField);
                    }
                    return space;
                });

        // Set an initial space for the player
        Space initialSpace = new Space(0, 0, null, board);
        player.setSpace(initialSpace);
        playerRepository.save(player);
    }

    @Test
    public void testPlayerReachesCheckpoint() {
        // Move player to the checkpoint space
        Space checkpointSpace = spaceRepository.findByXAndY(2, 2);
        player.setSpace(checkpointSpace);

        // Check and update the checkpoint
        PlayerServiceImpl playerServiceImpl = (PlayerServiceImpl) playerService;
        playerServiceImpl.checkAndUpdateCheckpoint(player);

        // Retrieve the updated player
        Optional<Player> updatedPlayerOptional = playerRepository.findById(player.getId());
        assertNotNull(updatedPlayerOptional);
        Player updatedPlayer = updatedPlayerOptional.get();

        // Verify the last checkpoint
        assertEquals(checkpointSpace, updatedPlayer.getLastCheckpoint());
    }

    @Test
    public void testPlayerResetsToLastCheckpoint() {
        // Move player to the checkpoint space and update the checkpoint
        Space checkpointSpace = spaceRepository.findByXAndY(2, 2);
        player.setSpace(checkpointSpace);
        PlayerServiceImpl playerServiceImpl = (PlayerServiceImpl) playerService;
        playerServiceImpl.checkAndUpdateCheckpoint(player);

        // Simulate player needing to reset to the last checkpoint
        playerServiceImpl.resetPlayerToLastCheckpoint(player);

        // Retrieve the updated player
        Optional<Player> updatedPlayerOptional = playerRepository.findById(player.getId());
        assertNotNull(updatedPlayerOptional);
        Player updatedPlayer = updatedPlayerOptional.get();

        // Verify the player is at the last checkpoint
        assertEquals(checkpointSpace, updatedPlayer.getSpace());
    }
}
