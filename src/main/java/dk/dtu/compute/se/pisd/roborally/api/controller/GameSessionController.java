package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
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

        if (gameSession.getPlayers() == null || gameSession.getPlayers().size() != 1) {
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

    @PostMapping("/{gameId}/join")
    public ResponseEntity<GameSession> joinGameSession(@PathVariable Long gameId, @RequestBody Player player) {
        GameSession updatedGameSession = gameSessionService.joinGameSession(gameId, player);
        return ResponseEntity.ok(updatedGameSession);
    }

    @PostMapping("/join/{joinCode}")
    public ResponseEntity<GameSession> joinGameSessionByCode(@PathVariable String joinCode, @RequestBody Player player) {
        GameSession updatedGameSession = gameSessionService.joinGameSessionByCode(joinCode, player);
        return ResponseEntity.ok(updatedGameSession);
    }

    @PostMapping("/{gameId}/players/{playerId}/ready")
    public ResponseEntity<GameSession> markPlayerReady(@PathVariable Long gameId, @PathVariable Long playerId) {
        GameSession updatedGameSession = gameSessionService.markPlayerReady(gameId, playerId);
        return ResponseEntity.ok(updatedGameSession);
    }

    @GetMapping("/{gameId}/ready")
    public ResponseEntity<Boolean> areAllPlayersReady(@PathVariable Long gameId) {
        boolean allReady = gameSessionService.areAllPlayersReady(gameId);
        return ResponseEntity.ok(allReady);
    }
}
