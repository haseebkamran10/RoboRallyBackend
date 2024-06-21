package dk.dtu.compute.se.pisd.roborally.api.websocket;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerMoveDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class GameEventHandler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void broadcastPlayerMove(Long playerId, int x, int y) {
        PlayerMoveDTO message = new PlayerMoveDTO(playerId, x, y);
        messagingTemplate.convertAndSend("/topic/moves", message);
    }
}
