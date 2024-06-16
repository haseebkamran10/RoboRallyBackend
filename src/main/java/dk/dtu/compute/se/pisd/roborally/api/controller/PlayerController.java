package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @GetMapping
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        PlayerDTO playerDTO = playerService.getPlayerById(id);
        return ResponseEntity.ok(playerDTO);
    }

    @PostMapping("/create-player")
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        PlayerDTO savedPlayer = playerService.createPlayer(playerDTO);
        return ResponseEntity.ok(savedPlayer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlayerDTO> updatePlayer(@PathVariable Long id, @RequestBody PlayerDTO playerDTO) {
        PlayerDTO updatedPlayer = playerService.updatePlayer(id, playerDTO);
        return ResponseEntity.ok(updatedPlayer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable Long id) {
        playerService.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/move")
    public ResponseEntity<PlayerDTO> movePlayer(@PathVariable Long id, @RequestParam int steps) {
        PlayerDTO playerDTO = playerService.getPlayerById(id);
        if (playerDTO != null) {
            // Assuming the move logic is implemented in the service layer
            PlayerDTO updatedPlayer = playerService.movePlayer(id, steps);
            return ResponseEntity.ok(updatedPlayer);
        }
        return ResponseEntity.notFound().build();
    }
}
