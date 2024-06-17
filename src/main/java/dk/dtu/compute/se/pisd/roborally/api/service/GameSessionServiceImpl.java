package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.repository.BoardRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.GameSessionRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
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
        if (gameSession.getBoard() == null || gameSession.getBoard().getId() == null) {
            throw new IllegalArgumentException("Board ID must not be null");
        }

        // Fetch the board from the database to ensure it exists
        Board board = boardRepository.findById(gameSession.getBoard().getId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        gameSession.setBoard(board);

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

        return gameSessionRepository.save(gameSession);
    }


    @Override
    public GameSession updateGameSession(Long id, GameSession gameSessionDetails) {
        GameSession gameSession = getGameSessionById(id);
        if (gameSession != null) {
            gameSession.setBoard(gameSessionDetails.getBoard());
            gameSession.setPlayers(gameSessionDetails.getPlayers());
            gameSession.setNumberOfPlayers(gameSessionDetails.getNumberOfPlayers());
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

        Player attachedPlayer = playerRepository.findById(player.getId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        attachedPlayer.setGameSession(gameSession);
        gameSession.getPlayers().add(attachedPlayer);

        // Update the number of players in the game session
        gameSession.setNumberOfPlayers(gameSession.getPlayers().size());

        playerRepository.save(attachedPlayer);
        return gameSessionRepository.save(gameSession);
    }

    @Override
    public GameSession joinGameSessionByCode(String joinCode, Player player) {
        GameSession gameSession = gameSessionRepository.findByJoinCode(joinCode);
        if (gameSession == null) {
            throw new IllegalArgumentException("Game session not found");
        }

        Player attachedPlayer = playerRepository.findById(player.getId())
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        attachedPlayer.setGameSession(gameSession);
        attachedPlayer.setReady(false); // Set ready to false initially
        gameSession.getPlayers().add(attachedPlayer);

        // Update the number of players in the game session
        gameSession.setNumberOfPlayers(gameSession.getPlayers().size());

        playerRepository.save(attachedPlayer);
        return gameSessionRepository.save(gameSession);
    }

    @Override
    public GameSession markPlayerReady(Long gameId, Long playerId) {
        GameSession gameSession = getGameSessionById(gameId);
        if (gameSession == null) {
            throw new IllegalArgumentException("Game session not found");
        }

        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        if (!gameSession.getPlayers().contains(player)) {
            throw new IllegalArgumentException("Player does not belong to this game session");
        }

        player.setReady(true);
        playerRepository.save(player);

        if (gameSession.getPlayers().stream().allMatch(Player::isReady)) {
            startGame(gameSession);
        }

        return gameSession;
    }


    @Override
    public boolean areAllPlayersReady(Long gameId) {
        GameSession gameSession = getGameSessionById(gameId);
        if (gameSession == null) {
            throw new IllegalArgumentException("Game session not found");
        }

        return gameSession.getPlayers().stream().allMatch(Player::isReady);
    }

    private void startGame(GameSession gameSession) {
        // Logic to start the game
        System.out.println("Game is starting with all players ready!");
    }
}


