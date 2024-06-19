package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.Heading;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dk.dtu.compute.se.pisd.roborally.api.repository.SpaceRepository;


import java.util.List;
import java.util.Optional;

/**
 * Service implementation for managing Player entities.
 */
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private SpaceRepository spaceRepository;

    @Override
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @Override
    public Player getPlayerById(Long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        return optionalPlayer.orElse(null);
    }

    @Override
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    @Override
    public Player updatePlayer(Long id, Player playerDetails) {
        Player player = getPlayerById(id);
        if (player != null) {
            player.setName(playerDetails.getName());
            player.setAvatar(playerDetails.getAvatar());
            return playerRepository.save(player);
        }
        return null;
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public Player movePlayer(Long playerId, int x, int y) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            Space space = spaceRepository.findByXAndY(x, y);
            if (space != null) {
                player.setSpace(space);
                return playerRepository.save(player);
            }
        }
        return null;
    }

    @Override
    public Player changePlayerDirection(Long playerId, String direction) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            Heading heading;
            try {
                heading = Heading.valueOf(direction.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid direction: " + direction);
            }
            player.setHeading(heading);
            return playerRepository.save(player);
        }
        return null;
    }

    public Player jumpPlayer(Long playerId, int targetX, int targetY) {
        Player player = getPlayerById(playerId);
        if (player != null) {
            Space targetSpace = spaceRepository.findByXAndY(targetX, targetY);
            if (targetSpace != null && player.jump(targetSpace)) {
                return playerRepository.save(player);
            }
        }
        return null;
    }
}