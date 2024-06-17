package com.pleewson.poker.controller.wsController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Game;
import com.pleewson.poker.repository.PlayerRepository;
import com.pleewson.poker.service.GameService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class GameController {

    private final PlayerRepository playerRepository;
    private final GameService gameService;
    private Long currentPlayer;

    public GameController(PlayerRepository playerRepository, GameService gameService) {
        this.playerRepository = playerRepository;
        this.gameService = gameService;
        currentPlayer = 2L;
    }


    @MessageMapping("/game.addPlayer")
    @SendTo("/topic/game")
    public Map<String, Object> addPlayer(@Payload String playerIdJSON, SimpMessageHeaderAccessor headerAccessor) {
        log.info("--------------->  {}  <- playerId from JS", playerIdJSON);

        if (playerIdJSON == null) {
            throw new IllegalArgumentException("Player not found in session");
        }

        String playerIdStr = playerIdJSON.replace("\"", "");
        Player player = playerRepository.findById(Long.parseLong(playerIdStr)).orElseThrow(() -> new EntityNotFoundException());

        headerAccessor.getSessionAttributes().put("playerId", player.getId());

        //creating game
        Game game = gameService.getGame();
        if (game == null) {
            game = gameService.createNewGame();
        }

        gameService.addPlayer(player);

        log.info("---- playerList size --- {} ---- ", game.getPlayerList().size());

        //player number based on the current number of player in the game
        int playerNumber = game.getPlayerList().indexOf(player) + 1;

        Map<String, Object> mapJSON = new HashMap<>();
        mapJSON.put("nickname", player.getNickname());
        mapJSON.put("playerId", player.getId());
        mapJSON.put("numberOfPlayers", gameService.getGame().getPlayerList().size());
        mapJSON.put("currentPlayer", currentPlayer);
        mapJSON.put("playerNumber", playerNumber);

        return mapJSON;
    }


    @MessageMapping("/game.makeMove.1")
    @SendTo("/topic/game")
    public Map<String, Object> makeMovePlayer1(@Payload String moveTypeJSON) {
        if (currentPlayer.equals(1L)) {
//            gameService.makeMove
            currentPlayer = 2L;
        }

        Map<String, Object> mapJSON = new HashMap<>();
        mapJSON.put("currentPlayer", currentPlayer);
        return mapJSON;
    }


    @MessageMapping("/game.makeMove.2")
    @SendTo("/topic/game")
    public Map<String, Object> makeMovePlayer2(@Payload String moveTypeJSON) {
        if (currentPlayer.equals(2L)) {
//            gameService.makeMove
            currentPlayer = 1L;
        }

        Map<String, Object> mapJSON = new HashMap<>();
        mapJSON.put("currentPlayer", currentPlayer);
        return mapJSON;
    }


    //TODO^
    //        makeMove - operations

}
