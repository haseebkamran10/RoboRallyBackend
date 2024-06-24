package dk.dtu.compute.se.pisd.roborally.api.dto;

import java.util.List;

public class GameStateDTO {
    private boolean gameStarted;
    private List<PlayerDTO> players;

    // Getters and setters
    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    public List<PlayerDTO> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDTO> players) {
        this.players = players;
    }
}
