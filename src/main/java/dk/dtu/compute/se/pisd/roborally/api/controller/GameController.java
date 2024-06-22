package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.model.*;
import dk.dtu.compute.se.pisd.roborally.api.repository.BoardRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.GameSessionRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class GameController {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private BoardRepository boardRepository;

    // Method to start a new game
    public void startNewGame(int boardId, List<String> playerNames) {
        Optional<Board> optionalBoard = boardRepository.findById((long) boardId);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            GameSession gameSession = new GameSession();
            gameSession.setBoard(board);

            // Initialize players and place them on the board
            for (String playerName : playerNames) {
                Player player = new Player();
                player.setName(playerName);
                player.setGameSession(gameSession);
                player.setSpace(board.getSpace(0, 0)); // Start all players at (0, 0) for simplicity
                playerRepository.save(player);
            }

            gameSessionRepository.save(gameSession);
        }
    }

    // Method to move a player
    public boolean movePlayer(Long playerId, Heading direction) {
        Optional<Player> optionalPlayer = playerRepository.findById(playerId);
        if (optionalPlayer.isPresent()) {
            Player player = optionalPlayer.get();
            return player.move(direction);
        }
        return false;
    }

    // Method to handle advanced movement mechanics for the player
    public boolean handleAdvancedMovement(Player player, Heading direction) {
        Space targetSpace = player.getNextSpaceInDirection(player.getSpace(), direction);

        if (targetSpace != null) {
            // Handle jumping
            if (player.getSpace().isJumpPad() && player.jump(targetSpace)) {
                return true;
            }

            // Handle sliding
            if (player.getSpace().isIceTile() && player.slide(direction)) {
                return true;
            }

            // Normal movement
            if (!targetSpace.isObstacle() && player.consumeEnergy(1)) { // Assuming moving one space costs 1 energy
                player.setSpace(targetSpace);
                return true;
            }
        }
        return false;
    }

    // Method to activate space effects
    public void activateSpaceEffects(Board board, Player player) {
        for (Space space : board.getSpaces()) {
            space.activate(player);
        }
    }

    // Method to get the game session by ID
    public Optional<GameSession> getGameSession(Long gameSessionId) {
        return gameSessionRepository.findById(gameSessionId);
    }

    // Method to reset player to last checkpoint
    public void resetPlayerToLastCheckpoint(Player player) {
        Space lastCheckpoint = player.getLastCheckpoint();
        if (lastCheckpoint != null) {
            player.setSpace(lastCheckpoint);
        } else {
            // Reset to starting position if no checkpoint is found
            player.setSpace(player.getGameSession().getBoard().getSpace(0, 0));
        }
        playerRepository.save(player);
    }

    // Method to check if the player has reached a checkpoint and update accordingly
    public void checkAndUpdateCheckpoint(Player player) {
        Space currentSpace = player.getSpace();
        if (currentSpace.getActionField() != null && currentSpace.getActionField().getActionType() == ActionField.ActionType.CHECKPOINT) {
            player.setLastCheckpoint(currentSpace);
            playerRepository.save(player);
        }
    }
}
