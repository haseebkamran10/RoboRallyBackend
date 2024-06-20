package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.mapper.PlayerMapper;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
    public ResponseEntity<PlayerDTO> movePlayer(@PathVariable Long playerId, @RequestParam int x, @RequestParam int y) {
        System.out.println("Received movePlayer request with playerId: " + playerId + ", x: " + x + ", y: " + y);
        Player player = playerService.movePlayer(playerId, x, y);
        PlayerDTO playerDTO = playerMapper.playerToPlayerDTO(player);
        if (playerDTO != null) {
            // Broadcasting the move to all connected clients
            simpMessagingTemplate.convertAndSend("/topic/moves", playerDTO);
            return ResponseEntity.ok(playerDTO);
        } else {
            return ResponseEntity.badRequest().build();
        }
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
}