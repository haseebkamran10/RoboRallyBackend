package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.Board;

import java.util.List;

/**
 * Service interface for managing Board entities.
 */
public interface BoardService {

    List<BoardDTO> getAllBoards();

    BoardDTO getBoardById(Long id);

    BoardDTO createBoard(BoardDTO boardDTO);

    BoardDTO updateBoard(Long id, BoardDTO boardDTO);

    void deleteBoard(Long id);

    Board convertToEntity(BoardDTO boardDTO); // Add this method
}
