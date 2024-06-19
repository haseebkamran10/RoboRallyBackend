package dk.dtu.compute.se.pisd.roborally.api.dto;

public class PlayerMoveDTO {
    private Long playerId;
    private int x;
    private int y;

    // Constructors, Getters, and Setters
    public PlayerMoveDTO() {
    }

    public PlayerMoveDTO(Long playerId, int x, int y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
