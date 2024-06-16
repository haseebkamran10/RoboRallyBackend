package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
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
import java.util.stream.Collectors;

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
        GameSession gameSession = new GameSession();

        // Find the board by ID
        Board board = boardRepository.findById(gameSessionDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("Board not found"));
        gameSession.setBoard(board);

        // Find the host player by ID
        Player host = playerRepository.findById(gameSessionDTO.getHostId())
                .orElseThrow(() -> new IllegalArgumentException("Host not found"));
        gameSession.setHost(host);

        // Add the host to the players list
        List<Player> players = new ArrayList<>();
        players.add(host);
        gameSession.setPlayers(players);

        // Set the number of players
        gameSession.setNumberOfPlayers(1);

        // Generate a random join code
        gameSession.setJoinCode(String.format("%06d", (int) (Math.random() * 1000000)));

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

        gameSession.setNumberOfPlayers(gameSession.getPlayers().size());

        playerRepository.save(attachedPlayer);
        return gameSessionRepository.save(gameSession);
    }

    @Override
    public GameSessionDTO convertToDTO(GameSession gameSession) {
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setId(gameSession.getId());
        gameSessionDTO.setBoardId(gameSession.getBoard().getId());
        gameSessionDTO.setPlayerIds(gameSession.getPlayers().stream().map(Player::getId).collect(Collectors.toList()));
        gameSessionDTO.setPlayers(gameSession.getPlayers().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
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

    private PlayerDTO convertToDTO(Player player) {
        return new PlayerDTO(player.getId(), player.getName(), player.getAvatar());
    }
}
