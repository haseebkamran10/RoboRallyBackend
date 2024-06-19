package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.repository.BoardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class GameSessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
        board.setName("Test Board");
        boardRepository.save(board);
    }

    @Test
    void testCreateGameSession() throws Exception {
        GameSessionDTO gameSessionDTO = new GameSessionDTO();
        gameSessionDTO.setBoardId(board.getId());

        List<PlayerDTO> playerDTOs = new ArrayList<>();
        PlayerDTO player1 = new PlayerDTO();
        player1.setName("Player1");
        playerDTOs.add(player1);
        PlayerDTO player2 = new PlayerDTO();
        player2.setName("Player2");
        playerDTOs.add(player2);
        gameSessionDTO.setPlayers(playerDTOs);

        mockMvc.perform(post("/api/gamesessions/create-new-session")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"boardId\":" + board.getId() + ",\"players\":[{\"name\":\"Player1\"},{\"name\":\"Player2\"}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.board.id").value(board.getId()))
                .andExpect(jsonPath("$.players.length()").value(2))
                .andExpect(jsonPath("$.players[0].name").value("Player1"))
                .andExpect(jsonPath("$.players[1].name").value("Player2"));
    }
}
