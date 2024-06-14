package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import java.util.List;

public interface GameSessionService {
    List<GameSession> getAllGameSessions();
    GameSession getGameSessionById(Long id);
    GameSession createGameSession(GameSession gameSession);
    GameSession updateGameSession(Long id, GameSession gameSession);
    void deleteGameSession(Long id);
}
