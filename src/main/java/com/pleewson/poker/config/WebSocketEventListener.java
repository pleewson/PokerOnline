package com.pleewson.poker.config;

import com.pleewson.poker.entities.Player;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;


    //TODO - change session attribute, get playerId instead player Object and add it in GameController
    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Player player = (Player) headerAccessor.getSessionAttributes().get("player");

        if (player != null) {
            String nickname = player.getNickname();
            log.info("LOGGGGGEEEER -> WebSocket user disconnected: {}", nickname);
            messageTemplate.convertAndSend("topic/game", nickname + " has left the game");
        }
    }
}
