package com.pleewson.poker.controller.wsController;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Card;
import com.pleewson.poker.model.Game;
import com.pleewson.poker.repository.PlayerRepository;
import com.pleewson.poker.service.GameService;
import com.pleewson.poker.model.MoveRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        player.setCoins(500);
        headerAccessor.getSessionAttributes().put("playerId", player.getId());

        //create game
        Game game = gameService.getGame();
        if (game == null) {
            game = gameService.createNewGame();
        }

        gameService.addPlayer(player);
        gameService.getDeck().dealInitialCards(player);
        log.info("NEW PLAYER CARDS ->->-> " + player.getCards().toString()); //work

        if (gameService.getGame().getPlayerList().size() == 2) {
            gameService.startGame();
        }

        player.setPlayerNumber(gameService.getGame().getPlayerList().size()); // nowe

        log.info("---- playerList size --- {} ---- ", game.getPlayerList().size());
        log.info("------ PLAYER NICKNAME : {}   PLAYER NUMBER  -> {}", player.getNickname(), player.getPlayerNumber());

        return createGameStateResponse(player.getPlayerNumber());
    }


    @MessageMapping("/game.makeMove")
    @SendTo("/topic/game")
    public Map<String, Object> makeMove(@Payload MoveRequest moveRequest) {
        Game game = gameService.getGame();
        System.out.println("JSON makeMove     -=-=-=-  " + moveRequest);

        Player player = game.getPlayerList().stream()
                .filter(p -> p.getPlayerNumber() == (moveRequest.getPlayerNumber()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid player ID"));

        if (game.getCurrentPlayer() == player.getPlayerNumber()) {
            gameService.processMove(game, player, moveRequest);
            return createGameMoveResponse();
        }
        throw new IllegalStateException("Not the current player's turn");
    }


//    @MessageMapping("/")
    private Map<String, Object> createGameStateResponse(int playerNumber) {
        Game game = gameService.getGame();
        Map<String, Object> gameState = new HashMap<>();

        for (Player player : game.getPlayerList()) {
            List<String> cards = player.getCards().stream()
                    .map(card -> card.getRank() + "-" + card.getSuit())
                    .collect(Collectors.toList());
            gameState.put("player" + player.getPlayerNumber() + "Cards", cards);
        }

        gameState.put("gameStarted", game.isGameStarted());
        gameState.put("currentPlayer", game.getCurrentPlayer());
        gameState.put("nickname", game.getPlayerList().get(game.getPlayerList().size() - 1).getNickname());
        gameState.put("playerNumber", playerNumber);
        return gameState;
    }


    private Map<String, Object> createGameMoveResponse() {
        Game game = gameService.getGame();

        List<String> communityCards = gameService.getGame().getCommunityCards().stream()
                .map(Card::toString)
                .collect(Collectors.toList());

        log.info("!@!@!@!@! communityCards ->> " + communityCards);

        Map<String, Object> gameState = new HashMap<>();
        gameState.put("communityCards", communityCards);

        if (game.getRound() == 1) { //TODO separate this method
            gameState.put("numCommunityCards", 0);
        }else if(game.getRound() == 2){
            gameState.put("numCommunityCards", 3);
        }else if(game.getRound() == 3) {
            gameState.put("numCommunityCards", 4);
        }else if(game.getRound() == 4) {
            gameState.put("numCommunityCards", 5);
        }

        gameState.put("currentBet", game.getCurrentBet());
        gameState.put("currentPlayer", game.getCurrentPlayer());

        List<Map<String, Object>> playersStats = game.getPlayerList().stream()
                .map(player -> {
                    Map<String, Object> playerData = new HashMap<>();
                    playerData.put("playerNumber", player.getPlayerNumber());
                    playerData.put("coins", player.getCoins());
                    playerData.put("currentBet", player.getCurrentBet());
                    return playerData;
                }).collect(Collectors.toList());

        //player cards - TODO separate this method
        for (Player player : game.getPlayerList()) {
            List<String> cards = player.getCards().stream()
                    .map(card -> card.getRank() + "-" + card.getSuit())
                    .collect(Collectors.toList());
            log.info("Player {} cards: {} ", player.getNickname(), cards);
            gameState.put("player" + player.getPlayerNumber() + "Cards", cards);
        }
        gameState.put("playersStats", playersStats);
        log.info("Game State: {}", gameState);

        return gameState;
    }
}
