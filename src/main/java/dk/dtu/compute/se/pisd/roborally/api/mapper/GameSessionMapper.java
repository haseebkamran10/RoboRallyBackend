package dk.dtu.compute.se.pisd.roborally.api.mapper;

import dk.dtu.compute.se.pisd.roborally.api.dto.GameSessionDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.GameSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface GameSessionMapper {
    GameSessionMapper INSTANCE = Mappers.getMapper(GameSessionMapper.class);

    @Mapping(source = "board.id", target = "boardId")
    @Mapping(source = "host.id", target = "hostId")
    GameSessionDTO gameSessionToGameSessionDTO(GameSession gameSession);

    @Mapping(source = "boardId", target = "board.id")
    @Mapping(source = "hostId", target = "host.id")
    GameSession gameSessionDTOToGameSession(GameSessionDTO gameSessionDTO);
}
