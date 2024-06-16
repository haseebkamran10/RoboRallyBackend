package dk.dtu.compute.se.pisd.roborally.api.controller;

import dk.dtu.compute.se.pisd.roborally.api.dto.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.api.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        BoardDTO board = boardService.getBoardById(id);
        return ResponseEntity.ok(board);
    }

    @PostMapping
    public ResponseEntity<BoardDTO> createBoard(@RequestBody BoardDTO boardDTO) {
        BoardDTO savedBoard = boardService.createBoard(boardDTO);
        return ResponseEntity.ok(savedBoard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardDTO> updateBoard(@PathVariable Long id, @RequestBody BoardDTO boardDTO) {
        BoardDTO updatedBoard = boardService.updateBoard(id, boardDTO);
        return ResponseEntity.ok(updatedBoard);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long id) {
        boardService.deleteBoard(id);
        return ResponseEntity.noContent().build();
    }
}
