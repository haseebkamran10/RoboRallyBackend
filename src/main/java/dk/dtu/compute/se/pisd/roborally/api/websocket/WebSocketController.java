package dk.dtu.compute.se.pisd.roborally.api.websocket;

import dk.dtu.compute.se.pisd.roborally.api.dto.PlayerMoveDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/move")
    @SendTo("/topic/moves")
    public PlayerMoveDTO movePlayer(PlayerMoveDTO move) {
        // Additional logic if needed
        return move;
    }
}
