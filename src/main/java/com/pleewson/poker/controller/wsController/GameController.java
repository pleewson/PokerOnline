package com.pleewson.poker.controller.wsController;

import com.pleewson.poker.model.Game;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class GameController {

    @MessageMapping("/game.nextPlayer")
    @SendTo("/topic/game")
    public Game nextPlayer(@Payload Game game) {

        return game;
    }



    @MessageMapping("/game.addPlayer")
    @SendTo("/topic/game")
    public Game addPlayer(@Payload String playerId, SimpMessageHeaderAccessor headerAccessor) {

        log.info("--------------->  {}  <- playerId from JS", playerId);
//        if (player == null) {
//            throw new IllegalArgumentException("Player not found in session");
//        }
//        game.addPlayer(player);
//        headerAccessor.getSessionAttributes().put("player", player);

        Game game = new Game();
        return game;
    }


}
