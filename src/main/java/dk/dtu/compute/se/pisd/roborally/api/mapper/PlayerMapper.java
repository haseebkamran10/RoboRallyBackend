package dk.dtu.compute.se.pisd.roborally.api.mapper;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerDTO;
import dk.dtu.compute.se.pisd.roborally.api.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    @Mapping(source = "gameSession.id", target = "gameSessionId")
    PlayerDTO playerToPlayerDTO(Player player);

    @Mapping(source = "gameSessionId", target = "gameSession.id")
    Player playerDTOToPlayer(PlayerDTO playerDTO);
}
