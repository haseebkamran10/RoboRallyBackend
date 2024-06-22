package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerMoveDTO;
import dk.dtu.compute.se.pisd.roborally.api.mapper.PlayerMapper;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    @GetMapping
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers().stream()
                .map(playerMapper::playerToPlayerDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        return ResponseEntity.ok(playerMapper.playerToPlayerDTO(player));
    }

    @PostMapping("/create-new-player")
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerMapper.playerDTOToPlayer(playerDTO);
        // Ensure no game session is set
        player.setGameSession(null);
        Player savedPlayer = playerService.createPlayer(player);
        return ResponseEntity.ok(playerMapper.playerToPlayerDTO(savedPlayer));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody PlayerDTO playerDTO) {
        Player playerDetails = playerMapper.playerDTOToPlayer(playerDTO);
        Player updatedPlayer = playerService.updatePlayer(id, playerDetails);
        return ResponseEntity.ok(playerMapper.playerToPlayerDTO(updatedPlayer));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{playerId}/move")
    public void movePlayer(@PathVariable Long playerId, @RequestParam int x, @RequestParam int y) {
        System.out.println("DEBUG: Attempting to move player with ID: " + playerId + " to coordinates: (" + x + ", " + y + ")");
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        // Set the new coordinates
        player.setX(x);
        player.setY(y);
        playerRepository.save(player);

        // Create a PlayerMoveDTO for the REST call
        PlayerMoveDTO playerMoveDTO = new PlayerMoveDTO();
        playerMoveDTO.setPlayerId(player.getId());
        playerMoveDTO.setX(x);
        playerMoveDTO.setY(y);

        // Notify the game of the move
        restTemplate.postForObject("http://localhost:8080/api/players/receive-move", playerMoveDTO, Void.class);
    }

    @PutMapping("/{id}/change-direction")
    public ResponseEntity<PlayerDTO> changePlayerDirection(@PathVariable Long id, @RequestParam String direction) {
        Player updatedPlayer = playerService.changePlayerDirection(id, direction);
        return ResponseEntity.ok(playerMapper.playerToPlayerDTO(updatedPlayer));
    }

    @PutMapping("/{id}/jump")
    public ResponseEntity<PlayerDTO> jumpPlayer(@PathVariable Long id, @RequestParam int targetX, @RequestParam int targetY) {
        Player updatedPlayer = playerService.jumpPlayer(id, targetX, targetY);
        return ResponseEntity.ok(playerMapper.playerToPlayerDTO(updatedPlayer));
    }

    @PostMapping("/receive-move")
    public ResponseEntity<Void> receiveMove(@RequestBody PlayerMoveDTO playerMoveDTO) {
        // Handle the received move here
        // You might want to update the local player's state or simply acknowledge the move
        // This method would be called on each client's side to receive the move broadcasted by other clients
        System.out.println("Move received for player ID: " + playerMoveDTO.getPlayerId() + " to coordinates: (" + playerMoveDTO.getX() + ", " + playerMoveDTO.getY() + ")");
        return ResponseEntity.ok().build();
    }
}
