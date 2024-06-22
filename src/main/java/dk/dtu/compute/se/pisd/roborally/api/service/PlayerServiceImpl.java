package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.ActionField;
import dk.dtu.compute.se.pisd.roborally.api.model.Heading;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.websocket.GameEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing Player entities.
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private GameEventHandler gameEventHandler;

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player getPlayerById(Long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        return optionalPlayer.orElse(null);
    }

    @Override
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player playerDetails) {
        Player player = getPlayerById(id);
        if (player != null) {
            player.setName(playerDetails.getName());
            player.setAvatar(playerDetails.getAvatar());
            return playerRepository.save(player);
        }
        return null;
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public Player movePlayer(Long playerId, int x, int y) {
        System.out.println("DEBUG: Attempting to move player with ID: " + playerId + " to coordinates: (" + x + ", " + y + ")");
        Player player = getPlayerById(playerId);
        if (player == null) {
            System.out.println("ERROR: Player not found with ID: " + playerId);
            throw new RuntimeException("Player not found with ID: " + playerId);
        }
        Space space = spaceRepository.findByXAndY(x, y);
        if (space == null) {
            System.out.println("ERROR: Space not found at coordinates (" + x + ", " + y + ")");
            throw new RuntimeException("Space not found at coordinates (" + x + ", " + y + ")");
        }
        player.setSpace(space);
        playerRepository.save(player);

        // Check and update the checkpoint if applicable
        checkAndUpdateCheckpoint(player);

        // Broadcast the move to all connected clients
        gameEventHandler.broadcastPlayerMove(playerId, x, y);

        return player;
    }

    @Override
    public Player changePlayerDirection(Long playerId, String direction) {
        Player player = getPlayerById(playerId);
        try {
            Heading heading = Heading.valueOf(direction.toUpperCase());
            player.setHeading(heading);
            return playerRepository.save(player);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid direction: " + direction);
        }
    }

    @Override
    public Player jumpPlayer(Long playerId, int targetX, int targetY) {
        Player player = getPlayerById(playerId);
        Space targetSpace = spaceRepository.findByXAndY(targetX, targetY);
        if (targetSpace == null) {
            throw new RuntimeException("Space not found at coordinates (" + targetX + ", " + targetY + ")");
        }
        if (!player.jump(targetSpace)) {
            throw new RuntimeException("Player jump failed");
        }
        return playerRepository.save(player);
    }

    // Method to check and update the player's checkpoint
    public void checkAndUpdateCheckpoint(Player player) {
        Space currentSpace = player.getSpace();
        if (currentSpace.getActionField() != null && currentSpace.getActionField().getActionType() == ActionField.ActionType.CHECKPOINT) {
            player.setLastCheckpoint(currentSpace);
            playerRepository.save(player);
        }
    }

    // Method to reset the player to the last checkpoint
    public void resetPlayerToLastCheckpoint(Player player) {
        Space lastCheckpoint = player.getLastCheckpoint();
        if (lastCheckpoint != null) {
            player.setSpace(lastCheckpoint);
            playerRepository.save(player);
        }
    }
}
