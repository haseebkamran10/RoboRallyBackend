package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.service.GameSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gamesessions")
public class GameSessionController {

    @Autowired
    private GameSessionService gameSessionService;

    @GetMapping
    public List<GameSession> getAllGameSessions() {
        return gameSessionService.getAllGameSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameSession> getGameSessionById(@PathVariable Long id) {
        GameSession gameSession = gameSessionService.getGameSessionById(id);
        return ResponseEntity.ok(gameSession);
    }

    @PostMapping
    public ResponseEntity<GameSession> createGameSession(@RequestBody GameSession gameSession) {
        if (gameSession.getBoard() == null || gameSession.getBoard().getId() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        GameSession savedGameSession = gameSessionService.createGameSession(gameSession);
        return ResponseEntity.ok(savedGameSession);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GameSession> updateGameSession(@PathVariable Long id, @RequestBody GameSession gameSessionDetails) {
        GameSession updatedGameSession = gameSessionService.updateGameSession(id, gameSessionDetails);
        return ResponseEntity.ok(updatedGameSession);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGameSession(@PathVariable Long id) {
        gameSessionService.deleteGameSession(id);
        return ResponseEntity.noContent().build();
    }
}
