package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;

import java.util.List;

public interface GameSessionService {
    List<GameSession> getAllGameSessions();
    GameSession getGameSessionById(Long id);
    GameSession createGameSession(GameSession gameSession);
    GameSession updateGameSession(Long id, GameSession gameSession);
    void deleteGameSession(Long id);

    GameSession joinGameSession(Long gameId, Player player);

    GameSession joinGameSessionByCode(String joinCode, Player player);
    void checkAndStartGame(GameSession gameSession); // New method to check and start the game

}