package dk.dtu.compute.se.pisd.roborally;

import static org.assertj.core.api.Assertions.assertThat;

import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.repository.BoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class BoardEntityTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void testBoardEntity() {
        Board board = new Board();
        board.setName("Test Board");
        board = boardRepository.save(board);

        assertThat(board.getId()).isNotNull();
    }
}
