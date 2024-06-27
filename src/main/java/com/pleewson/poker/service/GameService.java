package com.pleewson.poker.service;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Deck;
import com.pleewson.poker.model.Game;
import com.pleewson.poker.model.MoveRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Getter
@Setter
@Service
@Slf4j
public class GameService {
    private Game game;
    private Deck deck;
    private  CompareMethods compareMethods;

    @Autowired
    public GameService(Game game, Deck deck, CompareMethods compareMethods) {
        this.game = game;
        this.deck = deck;
        this.compareMethods = compareMethods;
    }


    public Game createNewGame() {
        game = new Game();
        deck = new Deck();
        return game;
    }


    public void startGame() {
        game.setGameStarted(true);
    }


    public void addPlayer(Player player) {
        if (game.isGameStarted()) {
            throw new IllegalStateException("Game has already started.");
        }

        player.setPlayerNumber(game.getPlayerList().size() + 1);
        if (game.getPlayerList().size() >= 2) {
            throw new IllegalStateException("Game is full.");
        }
        game.addPlayer(player);
    }


    public void removePlayer(Long playerId) {
        game.getPlayerList().removeIf(player -> player.getId().equals(playerId));
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


    public void startNewRound() {
        log.info("Starting a new round");

        game.setRound(1);
        deck.initializeDeck();
        deck.shuffleDeck();
        game.setCommunityCards(new ArrayList<>());
        for (Player player : game.getPlayerList()) {
            player.setCards(null);
            deck.dealInitialCards(player);
        }
    }


    public void nextRound() {
        game.setRound(game.getRound() + 1);
        setPlayersCurrentBet0();
        if (game.getRound() == 5) {
            determineWinner(game);
            nextPlayer();
            game.setRound(1);
        }
        deck.dealCommunityCards(game);
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


    public void checkIfGameIsFinishedAndDetermineWinner() {
        List<Player> players = game.getPlayerList();
        Player player1 = players.get(0);
        Player player2 = players.get(1);

        if (player1.getCoins() < 10 && player1.getCurrentBet() == 0 && game.getCurrentBet() == 0) {
            player2.setTrophies(player2.getTrophies() + 1);
            log.info(player2.getNickname() + " won the game, +1 trophy");
            //TODO redirect players to scoreboard
        }

        if (player2.getCoins() < 10 && player2.getCurrentBet() == 0 && game.getCurrentBet() == 0) {
            player1.setTrophies(player1.getTrophies() + 1);
            log.info(player1.getNickname() + " won the game, +1 trophy");
            //TODO redirect players to scoreboard
        }
    }


    protected void sendCoinsToPlayer1(Player player1, Player player2) {
        player1.setCoins(player1.getCoins() + game.getCurrentBet() + player1.getCurrentBet() + player2.getCurrentBet());
        game.setCurrentBet(0);
        player1.setCurrentBet(0);
    }


    protected void sendCoinsToPlayer2(Player player1, Player player2) {
        player2.setCoins(player2.getCoins() + game.getCurrentBet() + player1.getCurrentBet() + player2.getCurrentBet());
        game.setCurrentBet(0);
        player2.setCurrentBet(0);
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
        }else if(compareMethods.areBothRoyalFlush(player1, player2)){
            log.info("DOUBLE ROYAL FLUSH");
            divideCoinsIfDraw(player1, player2);
        }
    }
}


