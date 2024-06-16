package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/gamesessions")
public class GameSessionController {

    private static final Logger logger = LoggerFactory.getLogger(GameSessionController.class);

    @Autowired
    private GameSessionService gameSessionService;

    @GetMapping
    public List<GameSessionDTO> getAllGameSessions() {
        logger.debug("Fetching all game sessions");
        List<GameSessionDTO> gameSessions = gameSessionService.getAllGameSessions().stream()
                .map(gameSessionService::convertToDTO)
                .collect(Collectors.toList());
        logger.debug("Fetched game sessions: {}", gameSessions);
        return gameSessions;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameSessionDTO> getGameSessionById(@PathVariable Long id) {
        logger.debug("Fetching game session with id: {}", id);
        GameSessionDTO gameSessionDTO = gameSessionService.convertToDTO(gameSessionService.getGameSessionById(id));
        logger.debug("Fetched game session: {}", gameSessionDTO);
        return ResponseEntity.ok(gameSessionDTO);
    }

    @PostMapping("/create-new-session")
    public ResponseEntity<GameSessionDTO> createGameSession(@RequestBody GameSessionDTO gameSessionDTO) {
        logger.debug("Creating game session: {}", gameSessionDTO);
        GameSessionDTO savedGameSession = gameSessionService.createGameSession(gameSessionDTO);
        logger.debug("Created game session: {}", savedGameSession);
        return ResponseEntity.ok(savedGameSession);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameSessionDTO> updateGameSession(@PathVariable Long id, @RequestBody GameSessionDTO gameSessionDTO) {
        GameSessionDTO updatedGameSession = gameSessionService.updateGameSession(id, gameSessionDTO);
        return ResponseEntity.ok(updatedGameSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameSession(@PathVariable Long id) {
        gameSessionService.deleteGameSession(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/join/{joinCode}")
    public ResponseEntity<GameSessionDTO> joinGameSessionByCode(@PathVariable String joinCode, @RequestBody PlayerIdRequest request) {
        Long playerId = request.getPlayerId();
        GameSessionDTO updatedGameSession = gameSessionService.convertToDTO(gameSessionService.joinGameSessionByCode(joinCode, playerId));
        return ResponseEntity.ok(updatedGameSession);
    }

    public static class PlayerIdRequest {
        private Long playerId;

        public Long getPlayerId() {
            return playerId;
        }

        public void setPlayerId(Long playerId) {
            this.playerId = playerId;
        }
    }
}
