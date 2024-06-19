package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.mapper.GameSessionMapper;
import dk.dtu.compute.se.pisd.roborally.api.mapper.PlayerMapper;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gamesessions")
public class GameSessionController {

    @Autowired
    private GameSessionService gameSessionService;

    private final GameSessionMapper gameSessionMapper = GameSessionMapper.INSTANCE;
    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    @GetMapping
    public List<GameSessionDTO> getAllGameSessions() {
        return gameSessionService.getAllGameSessions().stream()
                .map(gameSessionMapper::gameSessionToGameSessionDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameSessionDTO> getGameSessionById(@PathVariable Long id) {
        GameSession gameSession = gameSessionService.getGameSessionById(id);
        return ResponseEntity.ok(gameSessionMapper.gameSessionToGameSessionDTO(gameSession));
    }

    @PostMapping("/create-new-session")
    public ResponseEntity<GameSessionDTO> createGameSession(@RequestBody GameSessionDTO gameSessionDTO) {
        GameSession gameSession = gameSessionMapper.gameSessionDTOToGameSession(gameSessionDTO);
        if (gameSession.getBoard() == null || gameSession.getBoard().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        if (gameSession.getPlayers() == null || gameSession.getPlayers().size() != 1) {
            return ResponseEntity.badRequest().body(null);
        }

        GameSession savedGameSession = gameSessionService.createGameSession(gameSession);
        return ResponseEntity.ok(gameSessionMapper.gameSessionToGameSessionDTO(savedGameSession));
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameSessionDTO> updateGameSession(@PathVariable Long id, @RequestBody GameSessionDTO gameSessionDTO) {
        GameSession gameSessionDetails = gameSessionMapper.gameSessionDTOToGameSession(gameSessionDTO);
        GameSession updatedGameSession = gameSessionService.updateGameSession(id, gameSessionDetails);
        return ResponseEntity.ok(gameSessionMapper.gameSessionToGameSessionDTO(updatedGameSession));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameSession(@PathVariable Long id) {
        gameSessionService.deleteGameSession(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{gameId}/join")
    public ResponseEntity<GameSessionDTO> joinGameSession(@PathVariable Long gameId, @RequestBody PlayerDTO playerDTO) {
        Player player = playerMapper.playerDTOToPlayer(playerDTO);
        GameSession updatedGameSession = gameSessionService.joinGameSession(gameId, player);
        return ResponseEntity.ok(gameSessionMapper.gameSessionToGameSessionDTO(updatedGameSession));
    }

    @PostMapping("/join/{joinCode}")
    public ResponseEntity<GameSessionDTO> joinGameSessionByCode(@PathVariable String joinCode, @RequestBody PlayerDTO playerDTO) {
        Player player = playerMapper.playerDTOToPlayer(playerDTO);
        GameSession updatedGameSession = gameSessionService.joinGameSessionByCode(joinCode, player);
        return ResponseEntity.ok(gameSessionMapper.gameSessionToGameSessionDTO(updatedGameSession));
    }

    @PostMapping("/mark-ready")
    public ResponseEntity<GameSessionDTO> markPlayerReady(@RequestBody Map<String, Long> ids) {
        Long gameId = ids.get("gameId");
        Long playerId = ids.get("playerId");
        if (gameId == null || playerId == null) {
            return ResponseEntity.badRequest().body(null);
        }
        GameSession updatedGameSession = gameSessionService.markPlayerReady(gameId, playerId);
        return ResponseEntity.ok(gameSessionMapper.gameSessionToGameSessionDTO(updatedGameSession));
    }

    @GetMapping("/{gameId}/ready")
    public ResponseEntity<Boolean> areAllPlayersReady(@PathVariable Long gameId) {
        boolean allReady = gameSessionService.areAllPlayersReady(gameId);
        return ResponseEntity.ok(allReady);
    }
}
