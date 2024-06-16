package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import java.util.List;

public interface PlayerService {
    List<PlayerDTO> getAllPlayers();
    PlayerDTO getPlayerById(Long id);
    PlayerDTO createPlayer(PlayerDTO playerDTO);
    PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO);
    void deletePlayer(Long id);
    PlayerDTO movePlayer(Long id, int steps); // Added this method for moving the player
}
