package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.api.mapper.BoardMapper;
import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    private final BoardMapper boardMapper = BoardMapper.INSTANCE;

    @GetMapping
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards().stream()
                .map(boardMapper::boardToBoardDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        Board board = boardService.getBoardById(id);
        return ResponseEntity.ok(boardMapper.boardToBoardDTO(board));
    }

    @PostMapping("/create-new-board")
    public ResponseEntity<BoardDTO> createBoard(@RequestBody BoardDTO boardDTO) {
        if (boardDTO.getWidth() == 0) {
            boardDTO.setWidth(13);
        }
        if (boardDTO.getHeight() == 0) {
            boardDTO.setHeight(10);
        }

        Board board = boardMapper.boardDTOToBoard(boardDTO);
        // No need to set spaces here if not included in the payload
        Board savedBoard = boardService.createBoard(board);
        return ResponseEntity.ok(boardMapper.boardToBoardDTO(savedBoard));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable Long id, @RequestBody BoardDTO boardDTO) {
        Board boardDetails = boardMapper.boardDTOToBoard(boardDTO);
        if (boardDTO.getSpaces() != null) {
            boardDetails.setSpaces(boardMapper.mapSpaces(boardDTO.getSpaces(), boardDetails));
        }
        Board updatedBoard = boardService.updateBoard(id, boardDetails);
        return ResponseEntity.ok(boardMapper.boardToBoardDTO(updatedBoard));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
