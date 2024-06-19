package dk.dtu.compute.se.pisd.roborally;

import com.fasterxml.jackson.databind.ObjectMapper;
import dk.dtu.compute.se.pisd.roborally.api.controller.PlayerController;
import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.mapper.PlayerMapper;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import dk.dtu.compute.se.pisd.roborally.api.service.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlayerService playerService;

    @MockBean
    private PlayerMapper playerMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private PlayerDTO playerDTO;
    private Player player;

    @BeforeEach
    void setUp() {
        playerDTO = new PlayerDTO();
        playerDTO.setId(1L);
        playerDTO.setName("Test Player");
        playerDTO.setAvatar("Test Avatar");

        player = new Player();
        player.setId(1L);
        player.setName("Test Player");
        player.setAvatar("Test Avatar");
    }

    @Test
    void testMovePlayer() throws Exception {
        when(playerService.movePlayer(1L, 2, 3)).thenReturn(player);
        when(playerMapper.playerToPlayerDTO(any(Player.class))).thenReturn(playerDTO);

        mockMvc.perform(put("/api/players/1/move")
                        .param("x", "2")
                        .param("y", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(playerDTO.getId()))
                .andExpect(jsonPath("$.name").value(playerDTO.getName()))
                .andExpect(jsonPath("$.avatar").value(playerDTO.getAvatar()));
    }

    @Test
    void testChangePlayerDirection() throws Exception {
        when(playerService.changePlayerDirection(1L, "NORTH")).thenReturn(player);
        when(playerMapper.playerToPlayerDTO(any(Player.class))).thenReturn(playerDTO);

        mockMvc.perform(put("/api/players/1/change-direction")
                        .param("direction", "NORTH")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(playerDTO.getId()))
                .andExpect(jsonPath("$.name").value(playerDTO.getName()))
                .andExpect(jsonPath("$.avatar").value(playerDTO.getAvatar()));
    }

    @Test
    void testJumpPlayer() throws Exception {
        when(playerService.jumpPlayer(1L, 2, 3)).thenReturn(player);
        when(playerMapper.playerToPlayerDTO(any(Player.class))).thenReturn(playerDTO);

        mockMvc.perform(put("/api/players/1/jump")
                        .param("targetX", "2")
                        .param("targetY", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(playerDTO.getId()))
                .andExpect(jsonPath("$.name").value(playerDTO.getName()))
                .andExpect(jsonPath("$.avatar").value(playerDTO.getAvatar()));
    }
}
