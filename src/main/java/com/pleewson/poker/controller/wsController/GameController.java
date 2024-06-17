package com.pleewson.poker.controller.wsController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Game;
import com.pleewson.poker.repository.PlayerRepository;
import com.pleewson.poker.service.GameService;
import com.pleewson.poker.service.MoveRequest;
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

    public GameController(PlayerRepository playerRepository, GameService gameService) {
        this.playerRepository = playerRepository;
        this.gameService = gameService;
    }


    @MessageMapping("/game.addPlayer")
    @SendTo("/topic/game")
    public Map<String, Object> addPlayer(@Payload String playerId, SimpMessageHeaderAccessor headerAccessor) {
        log.info("--------------->  {}  <- playerId from JS", playerId);

        if (playerId == null) {
            throw new IllegalArgumentException("Player not found in session");
        }

        String playerIdStr = playerId.replace("\"", "");
        Player player = playerRepository.findById(Long.parseLong(playerIdStr)).orElseThrow(() -> new EntityNotFoundException());
        log.info("------ PLAYER NICKNAME :  " + player.getNickname());

        headerAccessor.getSessionAttributes().put("playerId", player.getId());

        //create game
        Game game = gameService.getGame();
        if (game == null) {
            game = gameService.createNewGame();
        }

        gameService.addPlayer(player);

        if (gameService.getGame().getPlayerList().size() == 2) {
            gameService.startGame();
        }

        log.info("---- playerList size --- {} ---- ", game.getPlayerList().size());

        return createGameStateResponse();
    }


    @MessageMapping("/game.makeMove")
    @SendTo("/topic/game")
    public Map<String, Object> makeMove(@Payload MoveRequest moveRequest) {
        Game game = gameService.getGame();

        System.out.println("JSON makeMove     -=-=-=-  " + moveRequest);

        if (game.getCurrentPlayer().equals(moveRequest.getPlayerId())) {
            gameService.nextPlayer();
            return createGameStateResponse();
        }

        throw new IllegalStateException("Not the current player's turn");

    }

    //TODO^
    //        makeMove - operations

    private Map<String, Object> createGameStateResponse() {
        Game game = gameService.getGame();

        Map<String, Object> gameState = new HashMap<>();
        gameState.put("playerList", game.getPlayerList());
        gameState.put("gameStarted", game.isGameStarted());
        gameState.put("communityCards", game.getCommunityCards());
        gameState.put("currentBet", game.getCurrentBet());
        gameState.put("currentPlayer", game.getCurrentPlayer()); //TODO - fix it
        gameState.put("nickname", game.getPlayerList().get(game.getPlayerList().size()-1).getNickname());

        return gameState;
    }
}
