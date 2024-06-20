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
    Player movePlayer(Long playerId, int x, int y);
    Player changePlayerDirection(Long playerId, String direction);
    Player jumpPlayer(Long playerId, int targetX, int targetY);



}
