package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerMoveDTO;
import dk.dtu.compute.se.pisd.roborally.api.mapper.PlayerMapper;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.repository.PlayerRepository;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    private PlayerRepository playerRepository;

    @Autowired
    private RestTemplate restTemplate;

    private final PlayerMapper playerMapper = PlayerMapper.INSTANCE;

    @GetMapping
    public List<PlayerDTO> getAllPlayers() {
        return playerService.getAllPlayers().stream()
                .map(player -> {
                    PlayerDTO playerDTO = playerMapper.playerToPlayerDTO(player);
                    playerDTO.setX(player.getX());
                    playerDTO.setY(player.getY());
                    return playerDTO;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDTO> getPlayerById(@PathVariable Long id) {
        Player player = playerService.getPlayerById(id);
        PlayerDTO playerDTO = playerMapper.playerToPlayerDTO(player);
        playerDTO.setX(player.getX());
        playerDTO.setY(player.getY());
        return ResponseEntity.ok(playerDTO);
    }

    @PostMapping("/create-new-player")
    public ResponseEntity<PlayerDTO> createPlayer(@RequestBody PlayerDTO playerDTO) {
        Player player = playerMapper.playerDTOToPlayer(playerDTO);
        player.setGameSession(null);
        player.setX(0); // Default x-coordinate
        player.setY(0); // Default y-coordinate
        Player savedPlayer = playerService.createPlayer(player);
        PlayerDTO savedPlayerDTO = playerMapper.playerToPlayerDTO(savedPlayer);
        savedPlayerDTO.setX(savedPlayer.getX());
        savedPlayerDTO.setY(savedPlayer.getY());
        return ResponseEntity.ok(savedPlayerDTO);
    }

    @PutMapping("/{playerId}/move")
    public void movePlayer(@PathVariable Long playerId, @RequestParam int x, @RequestParam int y) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        player.setX(x);
        player.setY(y);
        playerRepository.save(player);

        PlayerMoveDTO playerMoveDTO = new PlayerMoveDTO(player.getId(), x, y);
        restTemplate.postForObject("http://localhost:8080/api/players/receive-move", playerMoveDTO, Void.class);
    }

    @PostMapping("/receive-move")
    public ResponseEntity<Void> receiveMove(@RequestBody PlayerMoveDTO playerMoveDTO) {
        System.out.println("Move received for player ID: " + playerMoveDTO.getPlayerId() + " to coordinates: (" + playerMoveDTO.getX() + ", " + playerMoveDTO.getY() + ")");
        return ResponseEntity.ok().build();
    }
}
