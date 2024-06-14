package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.model.Board;

import java.util.List;

/**
 * Service interface for managing Board entities.
 */
public interface BoardService {

    List<Board> getAllBoards();

    Board getBoardById(Long id);

    Board createBoard(Board board);

    Board updateBoard(Long id, Board boardDetails);

    void deleteBoard(Long id);
}
