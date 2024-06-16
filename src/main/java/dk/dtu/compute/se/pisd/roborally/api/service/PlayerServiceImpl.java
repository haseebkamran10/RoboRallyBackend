package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    public List<PlayerDTO> getAllPlayers() {
        return playerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerDTO getPlayerById(Long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        return optionalPlayer.map(this::convertToDTO).orElse(null);
    }

    @Override
    public PlayerDTO createPlayer(PlayerDTO playerDTO) {
        Player player = convertToEntity(playerDTO);
        player = playerRepository.save(player); // Ensure the player is saved and ID is generated
        return convertToDTO(player);
    }

    @Override
    public PlayerDTO updatePlayer(Long id, PlayerDTO playerDTO) {
        Player player = getPlayerEntityById(id);
        if (player != null) {
            player.setName(playerDTO.getName());
            player.setAvatar(playerDTO.getAvatar());
            // Update other fields if needed
            playerRepository.save(player);
            return convertToDTO(player);
        }
        return null;
    }

    @Override
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }

    @Override
    public PlayerDTO movePlayer(Long id, int steps) {
        Player player = getPlayerEntityById(id);
        if (player != null) {
            player.move(steps);
            playerRepository.save(player);
            return convertToDTO(player);
        }
        return null;
    }

    private PlayerDTO convertToDTO(Player player) {
        return new PlayerDTO(player.getId(), player.getName(), player.getAvatar());
    }

    private Player convertToEntity(PlayerDTO playerDTO) {
        Player player = new Player();
        player.setName(playerDTO.getName());
        player.setAvatar(playerDTO.getAvatar());
        // Set other fields if needed
        return player;
    }

    private Player getPlayerEntityById(Long id) {
        Optional<Player> optionalPlayer = playerRepository.findById(id);
        return optionalPlayer.orElse(null);
    }
}
