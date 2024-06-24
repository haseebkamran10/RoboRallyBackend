package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import dk.dtu.compute.se.pisd.roborally.api.repository.BoardRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.GameSessionRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameSessionServiceImpl implements GameSessionService {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Override
    public List<GameSession> getAllGameSessions() {
        return gameSessionRepository.findAll();
    }

    @Override
    public GameSession getGameSessionById(Long id) {
        Optional<GameSession> optionalGameSession = gameSessionRepository.findById(id);
        return optionalGameSession.orElse(null);
    }

    @Override
    public GameSession createGameSession(GameSession gameSession) {
        System.out.println("Creating game session with details: " + gameSession);

        if (gameSession.getBoard() == null || gameSession.getBoard().getId() == null) {
            throw new IllegalArgumentException("Board ID must not be null");
        }

        // Fetch the board from the database to ensure it exists
        Board board = boardRepository.findById(gameSession.getBoard().getId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        // Log maxPlayers value
        System.out.println("Max Players: " + gameSession.getMaxPlayers());

        // Validate the maxPlayers field
        if (gameSession.getMaxPlayers() < 2 || gameSession.getMaxPlayers() > 6) {
            throw new IllegalArgumentException("The number of players must be between 2 and 6.");
        }

        gameSession.setBoard(board);

        // Initialize spaces for the board
        initializeSpacesForBoard(board);

        // Ensure players are attached to the current session
        List<Player> attachedPlayers = new ArrayList<>();
        for (Player player : gameSession.getPlayers()) {
            Player attachedPlayer = playerRepository.findById(player.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));
            attachedPlayer.setGameSession(gameSession);
            attachedPlayers.add(attachedPlayer);
        }
        gameSession.setPlayers(attachedPlayers);

        // Set the number of players
        gameSession.setNumberOfPlayers(attachedPlayers.size());

        // Set the host of the game session
        if (!attachedPlayers.isEmpty()) {
            gameSession.setHost(attachedPlayers.get(0));
        }

        // Generate a random 6-digit join code
        gameSession.setJoinCode(String.format("%06d", (int) (Math.random() * 1000000)));

        System.out.println("Saving game session: " + gameSession);
        return gameSessionRepository.save(gameSession);
    }
    @Override
    public GameSession updateGameSession(Long id, GameSession gameSessionDetails) {
        GameSession gameSession = getGameSessionById(id);
        if (gameSession != null) {
            gameSession.setBoard(gameSessionDetails.getBoard());
            gameSession.setPlayers(gameSessionDetails.getPlayers());
            gameSession.setNumberOfPlayers(gameSessionDetails.getNumberOfPlayers());
            gameSession.setMaxPlayers(gameSessionDetails.getMaxPlayers());
            return gameSessionRepository.save(gameSession);
        }
        return null;
    }

    @Override
    public void deleteGameSession(Long id) {
        gameSessionRepository.deleteById(id);
    }

    @Override
    public GameSession joinGameSession(Long gameId, Player player) {
        GameSession gameSession = getGameSessionById(gameId);
        if (gameSession == null) {
            throw new IllegalArgumentException("Game session not found");
        }

        if (gameSession.getPlayers().size() >= gameSession.getMaxPlayers()) {
            throw new IllegalArgumentException("Game session is already full");
        }

        Player attachedPlayer = playerRepository.findById(player.getId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        attachedPlayer.setGameSession(gameSession);
        gameSession.getPlayers().add(attachedPlayer);

        // Update the number of players in the game session
        gameSession.setNumberOfPlayers(gameSession.getPlayers().size());

        playerRepository.save(attachedPlayer);
        GameSession updatedGameSession = gameSessionRepository.save(gameSession);
        checkAndStartGame(updatedGameSession); // Check and start the game if full
        return updatedGameSession;
    }

    @Override
    public GameSession joinGameSessionByCode(String joinCode, Player player) {
        GameSession gameSession = gameSessionRepository.findByJoinCode(joinCode);
        if (gameSession == null) {
            throw new IllegalArgumentException("Game session not found");
        }

        if (gameSession.getPlayers().size() >= gameSession.getMaxPlayers()) {
            throw new IllegalArgumentException("Game session is already full");
        }

        Player attachedPlayer = playerRepository.findById(player.getId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        attachedPlayer.setGameSession(gameSession);
        gameSession.getPlayers().add(attachedPlayer);

        // Update the number of players in the game session
        gameSession.setNumberOfPlayers(gameSession.getPlayers().size());

        playerRepository.save(attachedPlayer);
        GameSession updatedGameSession = gameSessionRepository.save(gameSession);
        checkAndStartGame(updatedGameSession); // Check and start the game if full
        return updatedGameSession;
    }

    @Override
    public void checkAndStartGame(GameSession gameSession) {
        if (gameSession.getNumberOfPlayers() == gameSession.getMaxPlayers()) {
            // Logic to start the game
            System.out.println("Game session is full. Starting the game with session: " + gameSession.getId());
            gameSession.setGameStarted(true); // Set the gameStarted field to true
            gameSessionRepository.save(gameSession); // Save the updated game session
            // You can add more game starting logic here
        }
    }

    private void initializeSpacesForBoard(Board board) {
        System.out.println("Initializing spaces for the board: " + board.getId());
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                Space space = new Space();
                space.setX(x);
                space.setY(y);
                space.setBoard(board);
                spaceRepository.save(space);
                System.out.println("Saved space at (" + x + ", " + y + ")");
            }
        }
    }
}
