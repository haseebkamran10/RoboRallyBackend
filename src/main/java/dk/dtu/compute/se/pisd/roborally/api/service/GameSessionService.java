package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;

import java.util.List;

public interface GameSessionService {
    List<GameSession> getAllGameSessions();
    GameSession getGameSessionById(Long id);
    GameSessionDTO createGameSession(GameSessionDTO gameSessionDTO);
    GameSessionDTO updateGameSession(Long id, GameSessionDTO gameSessionDTO);
    void deleteGameSession(Long id);

    GameSession joinGameSession(Long gameId, Player player);
    GameSession joinGameSessionByCode(String joinCode, Player player);

    GameSessionDTO convertToDTO(GameSession gameSession);
}
