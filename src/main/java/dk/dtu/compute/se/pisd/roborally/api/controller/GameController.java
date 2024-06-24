package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameStateDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.mapper.PlayerMapper;
import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.repository.BoardRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.GameSessionRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/gamesessions")
public class GameController {

    @Autowired
    private GameSessionRepository gameSessionRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PlayerService playerService;

    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

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

    @PutMapping("/{id}/change-direction")
    public ResponseEntity<PlayerDTO> changePlayerDirection(@PathVariable Long id, @RequestParam String direction) {
        Player updatedPlayer = playerService.changePlayerDirection(id, direction);
        return ResponseEntity.ok(playerMapper.playerToPlayerDTO(updatedPlayer));
    }

    @PutMapping("/{id}/jump")
    public ResponseEntity<PlayerDTO> jumpPlayer(@PathVariable Long id, @RequestParam int targetX, @RequestParam int targetY) {
        Player updatedPlayer = playerService.jumpPlayer(id, targetX, targetY);
        return ResponseEntity.ok(playerMapper.playerToPlayerDTO(updatedPlayer));
    }

    @GetMapping("/{gameId}/state")
    public ResponseEntity<GameStateDTO> getGameState(@PathVariable Long gameId) {
        GameSession gameSession = gameSessionRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game session not found with ID: " + gameId));

        GameStateDTO gameStateDTO = new GameStateDTO();
        gameStateDTO.setGameStarted(gameSession.isGameStarted());
        gameStateDTO.setPlayers(gameSession.getPlayers().stream()
                .map(player -> {
                    PlayerDTO playerDTO = playerMapper.playerToPlayerDTO(player);
                    playerDTO.setX(player.getX()); // Set x coordinate
                    playerDTO.setY(player.getY()); // Set y coordinate
                    return playerDTO;
                })
                .collect(Collectors.toList()));

        return ResponseEntity.ok(gameStateDTO);
    }

}
