package com.pleewson.poker.config;

import com.pleewson.poker.model.Game;
import com.pleewson.poker.service.GameService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class WebSocketEventListener {
    private final SimpMessageSendingOperations messageTemplate;
    private final GameService gameService;

    public WebSocketEventListener(SimpMessageSendingOperations messageTemplate, GameService gameService) {
        this.messageTemplate = messageTemplate;
        this.gameService = gameService;
    }


    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Long playerId = (Long) headerAccessor.getSessionAttributes().get("playerId");

        if (playerId != null) {
            log.info("user disconnected with ID : {}", playerId);
            gameService.setGame(new Game());
            log.info("game has been stopped");

            Map<String, Object> disconnectMessage = new HashMap<>();
            disconnectMessage.put("type", "disconnect");
            messageTemplate.convertAndSend("/topic/game", disconnectMessage); //sending message to JS that user has benn disconnected
        }
    }
}
