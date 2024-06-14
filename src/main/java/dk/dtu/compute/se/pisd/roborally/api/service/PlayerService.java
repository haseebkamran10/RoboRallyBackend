package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.Player;

import java.util.List;

/**
 * Service interface for managing Player entities.
 */
public interface PlayerService {

    List<Player> getAllPlayers();

    Player getPlayerById(Long id);

    Player createPlayer(Player player);

    Player updatePlayer(Long id, Player playerDetails);

    void deletePlayer(Long id);
}
