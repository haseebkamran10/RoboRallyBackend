package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameSessionDTO;
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
    public GameSessionDTO createGameSession(GameSessionDTO gameSessionDTO) {
        GameSession gameSession = convertToEntity(gameSessionDTO);
        gameSession = gameSessionRepository.save(gameSession);
        return convertToDTO(gameSession);
    }

    @Override
    public GameSessionDTO updateGameSession(Long id, GameSessionDTO gameSessionDTO) {
        GameSession gameSession = getGameSessionById(id);
        if (gameSession != null) {
            gameSession.setBoard(boardRepository.findById(gameSessionDTO.getBoardId()).orElse(null));
            gameSession.setPlayers(new ArrayList<>());
            for (Long playerId : gameSessionDTO.getPlayerIds()) {
                Player player = playerRepository.findById(playerId).orElse(null);
                if (player != null) {
                    player.setGameSession(gameSession);
                    gameSession.getPlayers().add(player);
                }
            }
            gameSession.setNumberOfPlayers(gameSessionDTO.getNumberOfPlayers());
            gameSession = gameSessionRepository.save(gameSession);
        }
        return convertToDTO(gameSession);
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
        gameSession.getPlayers().add(attachedPlayer);

        // Update the number of players in the game session
        gameSession.setNumberOfPlayers(gameSession.getPlayers().size());

        playerRepository.save(attachedPlayer);
        return gameSessionRepository.save(gameSession);
    }

    @Override
    public GameSessionDTO convertToDTO(GameSession gameSession) {
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setId(gameSession.getId());
        gameSessionDTO.setBoardId(gameSession.getBoard().getId());
        gameSessionDTO.setPlayerIds(new ArrayList<>());
        for (Player player : gameSession.getPlayers()) {
            gameSessionDTO.getPlayerIds().add(player.getId());
        }
        gameSessionDTO.setNumberOfPlayers(gameSession.getNumberOfPlayers());
        gameSessionDTO.setJoinCode(gameSession.getJoinCode());
        gameSessionDTO.setHostId(gameSession.getHost().getId());
        return gameSessionDTO;
    }

    private GameSession convertToEntity(GameSessionDTO gameSessionDTO) {
        GameSession gameSession = new GameSession();
        Board board = boardRepository.findById(gameSessionDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        gameSession.setBoard(board);

        List<Player> players = new ArrayList<>();
        for (Long playerId : gameSessionDTO.getPlayerIds()) {
            Player player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new IllegalArgumentException("Player not found"));
            player.setGameSession(gameSession);
            players.add(player);
        }
        gameSession.setPlayers(players);
        gameSession.setNumberOfPlayers(players.size());
        gameSession.setHost(players.get(0));  // Assuming the first player is the host
        gameSession.setJoinCode(String.format("%06d", (int) (Math.random() * 1000000)));
        return gameSession;
    }
}
