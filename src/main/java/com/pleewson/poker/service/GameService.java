package com.pleewson.poker.service;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Deck;
import com.pleewson.poker.model.Game;
import com.pleewson.poker.model.MoveRequest;
import com.pleewson.poker.repository.PlayerRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Setter
@Service
@Slf4j
public class GameService {
    private Game game;
    private Deck deck;
    private CardsHistoryService cardsHistoryService;
    private CompareMethods compareMethods;
    private PlayerRepository playerRepository;
    private final SimpMessageSendingOperations messageTemplate;

    @Autowired
    public GameService(Game game, Deck deck, CompareMethods compareMethods, PlayerRepository playerRepository, SimpMessageSendingOperations messageTemplate, CardsHistoryService cardsHistoryService) {
        this.game = game;
        this.deck = deck;
        this.compareMethods = compareMethods;
        this.messageTemplate = messageTemplate;
        this.playerRepository = playerRepository;
        this.cardsHistoryService = cardsHistoryService;
    }


    public Game createNewGame() {
        game = new Game();
        deck = new Deck();
        return game;
    }


    public void startGame() {
        game.setGameStarted(true);
        deck.dealCommunityCards(game);

        cardsHistoryService.saveCardsHistory(
                game.getPlayerList().get(0).getCards(),
                game.getPlayerList().get(1).getCards(),
                game.getCommunityCards());
    }


    public void addPlayer(Player player) {
        if (game.isGameStarted()) {
            throw new IllegalStateException("Game has already started.");
        }

        player.setPlayerNumber(game.getPlayerList().size() + 1); //TODO separate this method

        if (game.getPlayerList().size() >= 2) {
            throw new IllegalStateException("Game is full.");
        }

        if (game.getPlayerList().contains(player)) {
            throw new IllegalStateException("You are already in game. Waiting for second player");
        }

        game.addPlayer(player);
    }


    public void nextPlayer() {
        if (game.getCurrentPlayer() == 1) {
            game.setCurrentPlayer(2);
        } else {
            game.setCurrentPlayer(1);
        }
    }


    public void processMove(Game game, Player player, MoveRequest moveRequest) {
        String moveType = moveRequest.getMoveType();
        Integer betAmount = moveRequest.getBetAmount();
        Player opponent = game.getPlayerList().stream().filter(p -> p.getPlayerNumber() != player.getPlayerNumber()).findFirst().orElseThrow(() -> new IllegalStateException("No opponent found"));

        switch (moveType) {
            case "bet": {
                log.info("playerNum {} moveType -> {} betAmount {} ", player.getPlayerNumber(), moveType, betAmount);

                if (betAmount > player.getCoins()) {
                    log.info("Player " + player.getPlayerNumber() + " has not enough coins to place bet");
                    break;
                }
                if (betAmount < 20) {
                    log.info("Can't place bet, minimum amount is 20");
                    break;
                }

                if ((player.getCurrentBet() + betAmount) == opponent.getCurrentBet()) {
                    player.setCoins(player.getCoins() - betAmount);
                    player.setCurrentBet(player.getCurrentBet() + betAmount);
                    game.setCurrentBet(game.getCurrentBet() + betAmount);
                    player.setCheck(true);
                    checkIf2PlayersChecked(player, opponent);
                    log.info("Player " + player.getPlayerNumber() + " has matched the opponent's bet and is now checked.");
                    log.info("is player checked {}  ", player.isCheck());
                    break;
                }

                if ((player.getCurrentBet() + betAmount) >= opponent.getCurrentBet()) {
                    game.setCurrentBet(game.getCurrentBet() + betAmount);
                    player.setCurrentBet(player.getCurrentBet() + betAmount);
                    player.setCoins(player.getCoins() - betAmount);
                    player.setCheck(false);
                    nextPlayer();
                } else {
                    log.info("Bet must be at least equal opponent bet");
                }
                log.info("player " + player.getPlayerNumber() + " money after bet: " + player.getCoins());
                break;
            }
            case "check": {
                log.info("playerNum {} moveType -> {} ", player.getPlayerNumber(), moveType);

                if (opponent.getCurrentBet() < 20) {
                    log.info("Can't check. Must place the bet");
                    break;
                }

                player.setCheck(true);

                if (player.getCurrentBet().equals(opponent.getCurrentBet())) {
                    checkIf2PlayersChecked(player, opponent);
                    log.info("CHECK - 1");
                    break;

                } else if ((player.getCoins() + player.getCurrentBet()) < opponent.getCurrentBet()) {
                    int maxBet = player.getCoins() + player.getCurrentBet();
                    int difference = opponent.getCurrentBet() - maxBet;
                    player.setCurrentBet(maxBet); //MAX
                    opponent.setCoins(opponent.getCoins() + difference);
                    opponent.setCurrentBet(opponent.getCurrentBet() - difference);
                    game.setCurrentBet(player.getCoins() + game.getCurrentBet() - difference);
                    player.setCoins(0);
                    game.setRound(4); //last one
                    nextRound();
                    log.info("CHECK - 2");
                    //co jak wygra gracz co dal allina
                    break;

                } else {
                    int difference = opponent.getCurrentBet() - player.getCurrentBet();
                    player.setCoins(player.getCoins() - difference);
                    game.setCurrentBet(game.getCurrentBet() + difference);
                    player.setCurrentBet(opponent.getCurrentBet());
                    checkIf2PlayersChecked(player, opponent);
                    log.info("CHECK - 3");

                    break;
                }
            }
            case "fold": {
                log.info("playerNum {} moveType -> {} ", player.getPlayerNumber(), moveType);

                if (player.getCurrentBet() < 20) {
                    player.setCoins(player.getCoins() - 20);
                    game.setCurrentBet(game.getCurrentBet() + 20);
                    log.info("Player -20 coins");
                }

                opponent.setCoins(opponent.getCoins() + game.getCurrentBet());
                opponent.setCurrentBet(0);
                game.setCurrentBet(0);
                player.setCurrentBet(0);
                player.setCheck(false);
                opponent.setCheck(false);
                nextPlayer();

                startNewRound();
                log.info("FOLDED player" + player.getPlayerNumber() + " current coins: " + player.getCoins() + "  opponent coins: " + opponent.getCoins());
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid move type");
        }

        checkIfPlayersAllIn();
        checkIfGameIsFinishedAndDetermineWinner();
    }


    public void checkIf2PlayersChecked(Player player, Player opponent) {
        if (player.isCheck() && opponent.isCheck()) {
            setAllPlayersCheckFalse();
            nextPlayer();
            nextRound();
        } else {
            nextPlayer();
            log.info("CHECK -> playerCoins: {}, playerCurrentBet: {},  --- opponent.Coins: {}, opponentCurrentBet {}", player.getCoins(), player.getCurrentBet(), opponent.getCoins(), opponent.getCurrentBet());
        }
    }


    public void startNewRound() { //TODO change the name.
        log.info("Starting a new round");

        game.setRound(1);
        deck.setCards(deck.initializeDeck());
        deck.shuffleDeck();
        deck.dealCommunityCards(game);
        for (Player player : game.getPlayerList()) {
            player.setCards(null);
            deck.dealInitialCards(player);
        }
        cardsHistoryService.saveCardsHistory(
                game.getPlayerList().get(0).getCards(),
                game.getPlayerList().get(1).getCards(),
                game.getCommunityCards());
    }


    public void nextRound() {
        game.setRound(game.getRound() + 1);
        setPlayersCurrentBet0();
        if (game.getRound() == 5) {
            determineWinner(game);
            nextPlayer();
            startNewRound();
        }
    }


    public void setPlayersCurrentBet0() {
        for (Player player : game.getPlayerList()) {
            player.setCurrentBet(0);
        }
    }


    public void setAllPlayersCheckFalse() {
        for (Player player : game.getPlayerList()) {
            player.setCheck(false);
        }
    }


    public void checkIfPlayersAllIn() {
        List<Player> players = game.getPlayerList();
        if (players.get(0).getCoins() == 0 && players.get(1).getCoins() == 0 && game.getCurrentBet() == 1000) {
            log.info("ALL IN");
            nextRound();
            determineWinner(game);
        }
    }


    public void checkIfGameIsFinishedAndDetermineWinner() {
        List<Player> players = game.getPlayerList();
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        if (player1.getCoins() < 10 && player1.getCurrentBet() == 0 && game.getCurrentBet() == 0) {
            player2.winMatch();
            playerRepository.save(player2);
            log.info(player2.getNickname() + " won the game, +1 trophy");
            createNewGame();
            sendRedirectMessage();  //redirect players to scoreboards
        }

        if (player2.getCoins() < 10 && player2.getCurrentBet() == 0 && game.getCurrentBet() == 0) {
            player1.winMatch();
            playerRepository.save(player1);
            log.info(player1.getNickname() + " won the game, +1 trophy");
            createNewGame();
            sendRedirectMessage(); //redirect players to scoreboards
        }
    }

    private void sendRedirectMessage() {
        Map<String, Object> redirectMessage = new HashMap<>();
        redirectMessage.put("type", "finish");
        redirectMessage.put("url", "/scoreboard");

        messageTemplate.convertAndSend("/topic/game", redirectMessage);
    }


    protected void sendCoinsToPlayer1(Player player1, Player player2) {
        player1.setCoins(player1.getCoins() + game.getCurrentBet() + player1.getCurrentBet() + player2.getCurrentBet());
        game.setCurrentBet(0);
        player1.setCurrentBet(0);
        player2.setCurrentBet(0);
    }


    protected void sendCoinsToPlayer2(Player player1, Player player2) {
        player2.setCoins(player2.getCoins() + game.getCurrentBet() + player1.getCurrentBet() + player2.getCurrentBet());
        game.setCurrentBet(0);
        player2.setCurrentBet(0);
        player1.setCurrentBet(0);
    }


    protected void divideCoinsIfDraw(Player player1, Player player2) {
        log.info("DRAW - DIVIDED COINS");
        player1.setCoins(player1.getCoins() + (game.getCurrentBet() / 2));
        player2.setCoins(player2.getCoins() + (game.getCurrentBet() / 2));
        game.setCurrentBet(0);
    }


    public void determineWinner(Game game) {
        for (Player player : game.getPlayerList()) {
            player.setHandRank(EvaluateHand.evaluateHand(player.getCards(), game.getCommunityCards()).getRankValue());
        }

        Player player1 = game.getPlayerList().get(0);
        Player player2 = game.getPlayerList().get(1);

        if (player1.getHandRank() > player2.getHandRank()) {
            sendCoinsToPlayer1(player1, player2);
            log.info("WINNER player {} wins, playerCards {}, communityCards{}, handRank {}", player1.getNickname(), player1.getCards(), game.getCommunityCards(), player1.getHandRank());
            log.info("LOSER player {} wins, playerCards {}, communityCards{}, handRank {}", player2.getNickname(), player2.getCards(), game.getCommunityCards(), player2.getHandRank());
        } else if (player1.getHandRank() < player2.getHandRank()) {
            sendCoinsToPlayer2(player1, player2);
            log.info("WINNER player {} , playerCards {}, communityCards{}, handRank {}", player2.getNickname(), player2.getCards(), game.getCommunityCards(), player2.getHandRank());
            log.info("LOSER player {} , playerCards {}, communityCards{}, handRank {}", player1.getNickname(), player1.getCards(), game.getCommunityCards(), player1.getHandRank());

        } else if (compareMethods.areBothTwoHighCards(player1, player2)) {
            compareMethods.checkTheHighestCardRank(player1, player2);
        } else if (compareMethods.areBothPair(player1, player2)) {
            compareMethods.checkTheHighestPairRank(player1, player2);
        } else if (compareMethods.areBothTwoPairs(player1, player2)) {
            compareMethods.checkTheHighestTwoPairsRank(player1, player2);
        } else if (compareMethods.areBothThreeOfAKind(player1, player2)) {
            compareMethods.checkTheHighestThreeOfAKindRank(player1, player2);
        } else if (compareMethods.areBothStraight(player1, player2)) {
            compareMethods.checkTheHighestStraightRank(player1, player2);
        } else if (compareMethods.areBothFlush(player1, player2)) {
            compareMethods.checkTheHighestFlushRank(player1, player2);
        } else if (compareMethods.areBothFullHouse(player1, player2)) {
            compareMethods.checkTheHighestFullHouseRank(player1, player2);
        } else if (compareMethods.areBothFourOfAKind(player1, player2)) {
            compareMethods.checkTheHighestFourOfAKindRank(player1, player2);
        } else if (compareMethods.areBothStraightFlush(player1, player2)) {
            compareMethods.checkTheHighestStraightRank(player1, player2);
        } else if (compareMethods.areBothRoyalFlush(player1, player2)) {
            log.info("DOUBLE ROYAL FLUSH");
            divideCoinsIfDraw(player1, player2);
        }
    }
}


