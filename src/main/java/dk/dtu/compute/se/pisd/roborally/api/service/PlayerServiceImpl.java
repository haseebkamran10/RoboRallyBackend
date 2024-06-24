package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerMoveDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.*;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.websocket.GameEventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;
import org.springframework.web.client.RestTemplate;

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

    @Autowired
    private RestTemplate restTemplate;

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
        // Initialize x and y coordinates to default values (e.g., 0)
        player.setX(0);
        player.setY(0);
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player playerDetails) {
        Player player = getPlayerById(id);
        if (player != null) {
            player.setName(playerDetails.getName());
            player.setAvatar(playerDetails.getAvatar());
            player.setX(playerDetails.getX());
            player.setY(playerDetails.getY());
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
        GameSession gameSession = player.getGameSession();
        if (gameSession == null) {
            System.out.println("ERROR: Game session not found for player with ID: " + playerId);
            throw new RuntimeException("Game session not found for player with ID: " + playerId);
        }
        Board board = gameSession.getBoard();
        if (board == null) {
            System.out.println("ERROR: Board not found for game session with ID: " + gameSession.getId());
            throw new RuntimeException("Board not found for game session with ID: " + gameSession.getId());
        }
        Space space = spaceRepository.findByXAndYAndBoard(x, y, board);
        if (space == null) {
            System.out.println("ERROR: Space not found at coordinates (" + x + ", " + y + ") for board with ID: " + board.getId());
            throw new RuntimeException("Space not found at coordinates (" + x + ", " + y + ") for board with ID: " + board.getId());
        }

        player.setX(x);
        player.setY(y);
        playerRepository.save(player);

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
        if (player == null) {
            throw new RuntimeException("Player not found with ID: " + playerId);
        }
        GameSession gameSession = player.getGameSession();
        if (gameSession == null) {
            throw new RuntimeException("Game session not found for player with ID: " + playerId);
        }
        Board board = gameSession.getBoard();
        if (board == null) {
            throw new RuntimeException("Board not found for game session with ID: " + gameSession.getId());
        }
        Space targetSpace = spaceRepository.findByXAndYAndBoard(targetX, targetY, board);
        if (targetSpace == null) {
            throw new RuntimeException("Space not found at coordinates (" + targetX + ", " + targetY + ") for board with ID: " + board.getId());
        }
        if (!player.jump(targetSpace)) {
            throw new RuntimeException("Player jump failed");
        }
        return playerRepository.save(player);
    }
}
