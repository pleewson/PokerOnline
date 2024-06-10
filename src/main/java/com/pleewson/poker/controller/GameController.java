package com.pleewson.poker.controller;

import com.pleewson.poker.model.Game;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class GameController {

    @MessageMapping("/start")
    @SendTo("/topic/game")
    public Game startGame() {
        Game game = new Game();
        game.startGame();
        return game;
    }
}
