package dk.dtu.compute.se.pisd.roborally.api.service;

import dk.dtu.compute.se.pisd.roborally.api.dto.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.SpaceDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.ActionField;
import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import dk.dtu.compute.se.pisd.roborally.api.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class BoardServiceImpl implements BoardService {

    private static final Logger logger = LoggerFactory.getLogger(BoardServiceImpl.class);

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public List<BoardDTO> getAllBoards() {
        logger.debug("Retrieving all boards");
        return boardRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BoardDTO getBoardById(Long id) {
        logger.debug("Retrieving board with id: {}", id);
        return boardRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public BoardDTO createBoard(BoardDTO boardDTO) {
        logger.debug("Creating board with DTO: {}", boardDTO);
        Board board = convertToEntity(boardDTO);
        return convertToDTO(boardRepository.save(board));
    }

    @Override
    public BoardDTO updateBoard(Long id, BoardDTO boardDTO) {
        logger.debug("Updating board with id: {}", id);
        Board board = boardRepository.findById(id).orElse(null);
        if (board != null) {
            board.setName(boardDTO.getName());
            // Update other fields as needed
            boardRepository.save(board);
        }
        return convertToDTO(board);
    }

    @Override
    public void deleteBoard(Long id) {
        logger.debug("Deleting board with id: {}", id);
        boardRepository.deleteById(id);
    }

    private BoardDTO convertToDTO(Board board) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(board.getId());
        boardDTO.setName(board.getName());
        boardDTO.setSpaces(board.getSpaces().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList()));
        return boardDTO;
    }

    private SpaceDTO convertToDTO(Space space) {
        SpaceDTO spaceDTO = new SpaceDTO();
        spaceDTO.setId(space.getId());
        spaceDTO.setX(space.getX());
        spaceDTO.setY(space.getY());
        spaceDTO.setType(space.getType().name());
        return spaceDTO;
    }

    public Board convertToEntity(BoardDTO boardDTO) {
        Board board = new Board();
        board.setName(boardDTO.getName());
        if (boardDTO.getSpaces() != null) {
            board.setSpaces(boardDTO.getSpaces().stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList()));
        } else {
            board.setSpaces(new ArrayList<>());
        }
        return board;
    }

    private Space convertToEntity(SpaceDTO spaceDTO) {
        Space space = new Space();
        space.setX(spaceDTO.getX());
        space.setY(spaceDTO.getY());
        space.setType(ActionField.valueOf(spaceDTO.getType()));
        return space;
    }
}
