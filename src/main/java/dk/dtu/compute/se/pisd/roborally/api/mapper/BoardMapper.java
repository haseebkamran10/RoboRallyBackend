package dk.dtu.compute.se.pisd.roborally.api.mapper;

import dk.dtu.compute.se.pisd.roborally.api.dto.BoardDTO;
import dk.dtu.compute.se.pisd.roborally.api.dto.SpaceDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.ActionField;
import dk.dtu.compute.se.pisd.roborally.api.model.Board;
import dk.dtu.compute.se.pisd.roborally.api.model.Heading;
import dk.dtu.compute.se.pisd.roborally.api.model.Space;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface BoardMapper {
    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    @Mapping(target = "spaces", ignore = true) // Ignore spaces during board mapping
    BoardDTO boardToBoardDTO(Board board);

    @Mapping(target = "spaces", ignore = true) // Ignore spaces during board mapping
    Board boardDTOToBoard(BoardDTO boardDTO);

    default List<List<SpaceDTO>> mapSpaces(List<Space> spaces) {
        if (spaces == null || spaces.isEmpty()) {
            return new ArrayList<>();
        }

        int maxX = spaces.stream().mapToInt(Space::getX).max().orElse(0);
        int maxY = spaces.stream().mapToInt(Space::getY).max().orElse(0);

        List<List<SpaceDTO>> spaceGrid = new ArrayList<>();
        for (int i = 0; i <= maxX; i++) {
            List<SpaceDTO> row = new ArrayList<>();
            for (int j = 0; j <= maxY; j++) {
                row.add(null);
            }
            spaceGrid.add(row);
        }

        for (Space space : spaces) {
            SpaceDTO spaceDTO = new SpaceDTO();
            spaceDTO.setX(space.getX());
            spaceDTO.setY(space.getY());
            spaceDTO.setType(space.getActionField().getName());
            spaceDTO.setHeading(space.getHeading().toString());
            spaceDTO.setIndex(space.getIndex());

            spaceGrid.get(space.getX()).set(space.getY(), spaceDTO);
        }
        return spaceGrid;
    }

    default List<Space> mapSpaces(List<List<SpaceDTO>> spaceDTOs, Board board) {
        if (spaceDTOs == null || spaceDTOs.isEmpty()) {
            return new ArrayList<>();
        }

        List<Space> spaces = new ArrayList<>();
        for (List<SpaceDTO> row : spaceDTOs) {
            for (SpaceDTO spaceDTO : row) {
                if (spaceDTO != null) {
                    Space space = new Space();
                    space.setX(spaceDTO.getX());
                    space.setY(spaceDTO.getY());
                    space.setActionField(fetchActionFieldByName(spaceDTO.getType()));
                    space.setBoard(board);
                    space.setHeading(Heading.valueOf(spaceDTO.getHeading()));
                    space.setIndex(spaceDTO.getIndex());
                    spaces.add(space);
                }
            }
        }
        return spaces;
    }

    default ActionField fetchActionFieldByName(String name) {
        // Implement your logic to fetch ActionField by its name
        return new ActionField(); // Placeholder
    }
}
