package com.pleewson.poker.config;

import com.pleewson.poker.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemplate;

    @Autowired
    private GameService gameService;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Long playerId = (Long) headerAccessor.getSessionAttributes().get("playerId");

        if (playerId != null) {
            gameService.removePlayer(playerId);
            log.info("LOGGGGGEEEER -> WebSocket user disconnected with ID : {}", playerId);

            Map<String,Object> disconnectMessage = new HashMap<>();
            disconnectMessage.put("type", "disconnect");
            messageTemplate.convertAndSend("/topic/game", disconnectMessage);
        }
        //TODO STOP/RESET GAME IF ANY PLAYER DISCONNECT.
    }
}
