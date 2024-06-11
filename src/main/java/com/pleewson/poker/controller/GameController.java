package com.pleewson.poker.controller;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Game;
import jakarta.servlet.http.HttpSession;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class GameController {

    @MessageMapping("/game.nextPlayer")
    @SendTo("/topic/game")
    public Game nextPlayer(@Payload Game game) {
        return game;
    }

    @MessageMapping("/game.addPlayer")
    @SendTo("/topic/game")
    public Game addPlayer(@Payload Game game, SimpMessageHeaderAccessor headerAccessor, HttpSession session) {
        Player player = (Player) session.getAttribute("player");

        if(player == null){
            throw new IllegalArgumentException("Player not found in session");
        }

            game.addPlayer(player);
            headerAccessor.getSessionAttributes().put("player", player);

        return game;
    }
}
