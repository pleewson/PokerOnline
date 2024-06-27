package com.pleewson.poker.service;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Card;
import com.pleewson.poker.model.Deck;
import com.pleewson.poker.model.Game;
import com.pleewson.poker.model.PairInfo;
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

    @Autowired
    public GameService(Game game, Deck deck) {
        this.game = game;
        this.deck = deck;
    }

    public Game createNewGame() {
        game = new Game();
        deck = new Deck();
        return game;
    }

    public void addPlayer(Player player) {
        if (game.isGameStarted()) {
            throw new IllegalStateException("Game has already started.");
        }

        player.setPlayerNumber(game.getPlayerList().size() + 1);
//        if (game.getPlayerList().size() >= 2) {
//            throw new IllegalStateException("Game is full.");
//        }

        game.addPlayer(player);
    }

    public void removePlayer(Long playerId) {
        game.getPlayerList().removeIf(player -> player.getId().equals(playerId));
    }


    public void startGame() {
        game.setGameStarted(true);
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

    //123123123123123123213123213213213213123123123123123123123
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

        } else if (areBothTwoHighCards(player1, player2)) {
            checkTheHighestCardRank(player1, player2);
        } else if (areBothPair(player1, player2)) {
            checkTheHighestPairRank(player1, player2);
        } else if (areBothTwoPairs(player1, player2)) {
            checkTheHighestTwoPairsRank(player1, player2);
        } else if (areBothThreeOfAKind(player1, player2)) {
            checkTheHighestThreeOfAKindRank(player1, player2);
        } else if (areBothStraight(player1, player2)) {
            checkTheHighestStraightRank(player1, player2);
        } else if (areBothFlush(player1, player2)) {
            checkTheHighestFlushRank(player1, player2);
        } else if (areBothFullHouse(player1, player2)) {
            checkTheHighestFullHouseRank(player1, player2);
        } else if (areBothFourOfAKind(player1, player2)) {
            checkTheHighestFourOfAKindRank(player1, player2);
        } else if (areBothStraightFlush(player1, player2)) {
            checkTheHighestStraightRank(player1, player2);
        }else if(areBothRoyalFlush(player1, player2)){
            log.info("DOUBLE ROYAL FLUSH");
            divideCoinsIfDraw(player1, player2);
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


    private void sendCoinsToPlayer1(Player player1, Player player2) {
        player1.setCoins(player1.getCoins() + game.getCurrentBet() + player1.getCurrentBet() + player2.getCurrentBet());
        game.setCurrentBet(0);
        player1.setCurrentBet(0);
    }

    private void sendCoinsToPlayer2(Player player1, Player player2) {
        player2.setCoins(player2.getCoins() + game.getCurrentBet() + player1.getCurrentBet() + player2.getCurrentBet());
        game.setCurrentBet(0);
        player2.setCurrentBet(0);
    }

    private void divideCoinsIfDraw(Player player1, Player player2) {
        log.info("DRAW - DIVIDED COINS");
        player1.setCoins(player1.getCoins() + (game.getCurrentBet() / 2));
        player2.setCoins(player2.getCoins() + (game.getCurrentBet() / 2));
        game.setCurrentBet(0);
    }


    public boolean areBothTwoHighCards(Player player1, Player player2) {
        if (player1.getHandRank() == 1 && player2.getHandRank() == 1) {
            return true;
        }
        return false;
    }

    public boolean areBothPair(Player player1, Player player2) {
        if (player1.getHandRank() == 2 && player2.getHandRank() == 2) {
            return true;
        }
        return false;
    }

    public boolean areBothTwoPairs(Player player1, Player player2) {
        if (player1.getHandRank() == 3 && player2.getHandRank() == 3) {
            return true;
        }
        return false;
    }

    public boolean areBothThreeOfAKind(Player player1, Player player2) {
        if (player1.getHandRank() == 4 && player2.getHandRank() == 4) {
            return true;
        }
        return false;
    }


    public boolean areBothStraight(Player player1, Player player2) {
        if (player1.getHandRank() == 5 && player2.getHandRank() == 5) {
            return true;
        }
        return false;
    }

    public boolean areBothFlush(Player player1, Player player2) {
        if (player1.getHandRank() == 6 && player2.getHandRank() == 6) {
            return true;
        }
        return false;
    }

    public boolean areBothFullHouse(Player player1, Player player2) {
        if (player1.getHandRank() == 7 && player2.getHandRank() == 7) {
            return true;
        }
        return false;
    }

    public boolean areBothFourOfAKind(Player player1, Player player2) {
        if (player1.getHandRank() == 8 && player2.getHandRank() == 8) {
            return true;
        }
        return false;
    }

    public boolean areBothStraightFlush(Player player1, Player player2) {
        if (player1.getHandRank() == 9 && player2.getHandRank() == 9) {
            return true;
        }
        return false;
    }

    public boolean areBothRoyalFlush(Player player1, Player player2){
        if (player1.getHandRank() == 10 && player2.getHandRank() == 10) {
            return true;
        }
        return false;
    }


    public void checkTheHighestCardRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());

        //sort reversed to get the highest card first
        Collections.sort(player1Cards, Card.RANK_COMPARATOR.reversed());
        Collections.sort(player2Cards, Card.RANK_COMPARATOR.reversed());

        for (int i = 0; i < player1Cards.size() && i < player1Cards.size(); i++) {
            int player1CardRank = Card.rankToInt(player1Cards.get(i).getRank());
            int player2CardRank = Card.rankToInt(player2Cards.get(i).getRank());

            if (player1CardRank > player2CardRank) {
                sendCoinsToPlayer1(player1, player2);
                log.info("{} WIN with {}, {} LOSE with {}", player1.getNickname(), player1CardRank, player2.getNickname(), player2CardRank);
                return;
            } else if (player1CardRank < player2CardRank) {
                sendCoinsToPlayer2(player1, player2);
                log.info("{} WIN with {}, {} LOSE with {}", player2.getNickname(), player2CardRank, player1.getNickname(), player1CardRank);
                return;
            } else {
                divideCoinsIfDraw(player1, player2);
            }

        }
    }

    public void checkTheHighestKicker(Player player1, List<Card> player1Cards, Player player2, List<Card> player2Cards) {
        Collections.sort(player1Cards, Card.RANK_COMPARATOR.reversed());
        Collections.sort(player2Cards, Card.RANK_COMPARATOR.reversed());

        for (int i = 0; i < player1Cards.size(); i++) {
            int player1KickerRank = Card.rankToInt(player1Cards.get(i).getRank());
            int player2KickerRank = Card.rankToInt(player2Cards.get(i).getRank());

            if (player1KickerRank > player2KickerRank) {
                sendCoinsToPlayer1(player1, player2);
                log.info("{} WIN with kicker {}, {} LOSE with kicker {}",
                        player1.getNickname(), player1KickerRank, player2.getNickname(), player2KickerRank);
                return;
            } else if (player1KickerRank < player2KickerRank) {
                sendCoinsToPlayer2(player1, player2);
                log.info("{} WIN with kicker {}, {} LOSE with kicker {}",
                        player2.getNickname(), player2KickerRank, player1.getNickname(), player1KickerRank);
                return;
            }
        }
        divideCoinsIfDraw(player1, player2);
    }

    public void checkTheHighestPairRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(game.getCommunityCards());
        player2Cards.addAll(game.getCommunityCards());

        //sort reversed to get the highest card first
        Collections.sort(player1Cards, Card.RANK_COMPARATOR.reversed());
        Collections.sort(player2Cards, Card.RANK_COMPARATOR.reversed());

        int player1PairRank = findPairRank(player1Cards);
        int player2PairRank = findPairRank(player2Cards);

        if (player1PairRank > player2PairRank) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with pair of {}, {} LOSE with pair of {}", player1.getNickname(), player1PairRank, player2.getNickname(), player2PairRank);
            return;
        } else if (player1PairRank < player2PairRank) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with pair of {}, {} LOSE with pair of {}", player2.getNickname(), player2PairRank, player1.getNickname(), player1PairRank);
            return;
        } else {
            log.info("PAIR VALUE ARE THE SAME. starting checkTheHighestCardRank method...");
            checkTheHighestCardRank(player1, player2);
        }
    }

    public void checkTheHighestTwoPairsRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(game.getCommunityCards());
        player2Cards.addAll(game.getCommunityCards());

        //sort reversed to get the highest card first
        Collections.sort(player1Cards, Card.RANK_COMPARATOR.reversed());
        Collections.sort(player2Cards, Card.RANK_COMPARATOR.reversed());

        PairInfo player1PairInfo = findTwoPairs(player1Cards);
        PairInfo player2PairInfo = findTwoPairs(player2Cards);

        if (player1PairInfo.getHigherPairRank() > player2PairInfo.getHigherPairRank()) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                    player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank(),
                    player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank());
        } else if (player1PairInfo.getHigherPairRank() < player2PairInfo.getHigherPairRank()) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                    player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank(),
                    player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank());
        } else {
            if (player1PairInfo.getLowerPairRank() > player2PairInfo.getLowerPairRank()) {
                sendCoinsToPlayer1(player1, player2);
                log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                        player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank(),
                        player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank());
            } else if (player1PairInfo.getLowerPairRank() < player2PairInfo.getLowerPairRank()) {
                sendCoinsToPlayer2(player1, player2);
                log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                        player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank(),
                        player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank());
            } else {
                checkTheHighestCardRank(player1, player2);
            }
        }

    }

    public void checkTheHighestThreeOfAKindRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(game.getCommunityCards());
        player2Cards.addAll(game.getCommunityCards());

        //sort reversed to get the highest card first
        Collections.sort(player1Cards, Card.RANK_COMPARATOR.reversed());
        Collections.sort(player2Cards, Card.RANK_COMPARATOR.reversed());

        int player1ThreeOfAKindRank = findThreeOfAKindRank(player1Cards);
        int player2ThreeOfAKindRank = findThreeOfAKindRank(player2Cards);

        if (player1ThreeOfAKindRank > player2ThreeOfAKindRank) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with three of a kind {}, {} LOSE with three of a kind {}",
                    player1.getNickname(), player1ThreeOfAKindRank, player2.getNickname(), player2ThreeOfAKindRank);
        } else if (player1ThreeOfAKindRank < player2ThreeOfAKindRank) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with three of a kind {}, {} LOSE with three of a kind {}",
                    player2.getNickname(), player2ThreeOfAKindRank, player1.getNickname(), player1ThreeOfAKindRank);
        } else {
            //remove three of a kind to compare kickers
            removeCardsWithRank(player1Cards, player1ThreeOfAKindRank);
            removeCardsWithRank(player2Cards, player2ThreeOfAKindRank);

            checkTheHighestKicker(player1, player1Cards, player2, player2Cards);
        }
    }


    public void checkTheHighestStraightRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(game.getCommunityCards());
        player2Cards.addAll(game.getCommunityCards());

        int player1HighestStraightCard = findHighestStraightCard(player1Cards);
        int player2HighestStraightCard = findHighestStraightCard(player2Cards);

        if (player1HighestStraightCard > player2HighestStraightCard) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with straight {}, {} LOSE with straight {}",
                    player1.getNickname(), player1HighestStraightCard, player2.getNickname(), player2HighestStraightCard);
        } else if (player1HighestStraightCard < player2HighestStraightCard) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with straight {}, {} LOSE with straight {}",
                    player2.getNickname(), player2HighestStraightCard, player1.getNickname(), player1HighestStraightCard);
        } else {
            log.info("DRAW. BOTH PLAYERS HAVE SAME HIGHEST STRAIGHT CARD {} {} , {} {}", player1.getNickname(), player1HighestStraightCard, player2.getNickname(), player2HighestStraightCard);
            divideCoinsIfDraw(player1, player2);
        }
    }


    public void checkTheHighestFlushRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(game.getCommunityCards());
        player2Cards.addAll(game.getCommunityCards());

        //flush cards
        List<Card> player1FlushCards = findFlushCards(player1Cards);
        List<Card> player2FlushCards = findFlushCards(player2Cards);

        // Find the highest card in the flush for each player
        int player1FlushHighCard = findHighestCardInFlush(player1FlushCards);
        int player2FlushHighCard = findHighestCardInFlush(player2FlushCards);

        if (player1FlushHighCard > player2FlushHighCard) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with flush {}, {} LOSE with flush {}",
                    player1.getNickname(), player1FlushHighCard, player2.getNickname(), player2FlushHighCard);
        } else if (player1FlushHighCard < player2FlushHighCard) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with flush {}, {} LOSE with flush {}",
                    player2.getNickname(), player2FlushHighCard, player1.getNickname(), player1FlushHighCard);
        } else {
            //remove flush cards to compare kicker
            player1Cards.removeAll(player1FlushCards);
            player2Cards.removeAll(player2FlushCards);

            checkTheHighestKicker(player1, player1Cards, player2, player2Cards);
        }
    }


    public void checkTheHighestFullHouseRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(game.getCommunityCards());
        player2Cards.addAll(game.getCommunityCards());

        //sort reversed to get the highest card first
        Collections.sort(player1Cards, Card.RANK_COMPARATOR.reversed());
        Collections.sort(player2Cards, Card.RANK_COMPARATOR.reversed());

        int player1TripleRank = findThreeOfAKindRank(player1Cards);
        int player2TripleRank = findThreeOfAKindRank(player2Cards);
        int player1PairRank = findPairRank(player1Cards);
        int player2PairRank = findPairRank(player2Cards);

        if (player1TripleRank > player2TripleRank) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with full house {}, {} LOSE with full house {}",
                    player1.getNickname(), player1TripleRank, player1PairRank, player2.getNickname(), player2TripleRank, player2PairRank);
        } else if (player1TripleRank < player2TripleRank) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with full house {}, {} LOSE with full house {}",
                    player2.getNickname(), player2TripleRank, player2PairRank, player1.getNickname(), player1TripleRank, player1PairRank);
        } else if (player1PairRank > player2PairRank) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with full house {}, {} LOSE with full house {}",
                    player1.getNickname(), player1TripleRank, player1PairRank, player2.getNickname(), player2TripleRank, player2PairRank);
        } else if (player1PairRank < player2PairRank) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with full house {}, {} LOSE with full house {}",
                    player2.getNickname(), player2TripleRank, player2PairRank, player1.getNickname(), player1TripleRank, player1PairRank);
        } else {
            log.info("DRAW. Both triple and pair ranks are the same. {} full house rank ({} - {}),  {} full house rank ({} - {})",
                    player1.getNickname(), player1TripleRank, player1PairRank, player2.getNickname(), player2TripleRank, player2PairRank);
            divideCoinsIfDraw(player1, player2);
        }
    }


    public void checkTheHighestFourOfAKindRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(game.getCommunityCards());
        player2Cards.addAll(game.getCommunityCards());

        //sort reversed to get the highest card first
        Collections.sort(player1Cards, Card.RANK_COMPARATOR.reversed());
        Collections.sort(player2Cards, Card.RANK_COMPARATOR.reversed());

        int player1FourOfAKindRank = findFourOfAKindRank(player1Cards);
        int player2FourOfAKindRank = findFourOfAKindRank(player2Cards);

        if (player1FourOfAKindRank > player2FourOfAKindRank) {
            sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with four of a kind {}, {} LOSE with four of a kind {}",
                    player1.getNickname(), player1FourOfAKindRank, player2.getNickname(), player2FourOfAKindRank);
        } else if (player1FourOfAKindRank < player2FourOfAKindRank) {
            sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with four of a kind {}, {} LOSE with four of a kind {}",
                    player2.getNickname(), player2FourOfAKindRank, player1.getNickname(), player1FourOfAKindRank);
        } else {
            //remove four of a kind cards to compare kicker
            removeCardsWithRank(player1Cards, player1FourOfAKindRank);
            removeCardsWithRank(player2Cards, player2FourOfAKindRank);
            checkTheHighestKicker(player1, player1Cards, player2, player2Cards);
        }
    }


    public int findPairRank(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 2) {
                return Card.rankToInt(entry.getKey());
            }
        }
        return -1;
    }


    public int findThreeOfAKindRank(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 3) {
                return Card.rankToInt(entry.getKey());
            }
        }
        return -1;
    }


    public int findFourOfAKindRank(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 4) {
                return Card.rankToInt(entry.getKey());
            }
        }
        return -1;
    }


    public void removeCardsWithRank(List<Card> cards, int rank) {
        cards.removeIf(card -> Card.rankToInt(card.getRank()) == rank);
    }


    private PairInfo findTwoPairs(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        int higherPairRank = 0;
        int lowerPairRank = 0;

        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 2) {
                if (higherPairRank == 0) {
                    higherPairRank = Card.rankToInt(entry.getKey());
                } else {
                    lowerPairRank = Card.rankToInt(entry.getKey());
                }
            }
        }

        return new PairInfo(higherPairRank, lowerPairRank);
    }

    private int findHighestCardInFlush(List<Card> cards) {
        return Card.rankToInt(Collections.max(cards, Card.RANK_COMPARATOR).getRank());
    }


    public int findHighestStraightCard(List<Card> cards) {
        Set<Integer> ranks = new HashSet<>();
        for (Card card : cards) {
            ranks.add(Card.rankToInt(card.getRank()));
        }

        List<Integer> sortedRanks = new ArrayList<>(ranks);
        Collections.sort(sortedRanks);
        for (int i = 0; i <= sortedRanks.size() - 5; i++) {
            if (sortedRanks.get(i + 4) - sortedRanks.get(i) == 4) {
                return sortedRanks.get(i);
            }
        }

        // return highest rank when -> Ace as low card (A, 2, 3, 4, 5)
        if (ranks.contains(14) && ranks.contains(2) && ranks.contains(3) && ranks.contains(4) && ranks.contains(5)) {
            return 5;
        }
        return -1;
    }

    private List<Card> findFlushCards(List<Card> cards) {
        Map<String, List<Card>> suitMap = new HashMap<>();

        for (Card card : cards) {
            suitMap.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        for (List<Card> suitCards : suitMap.values()) {
            if (suitCards.size() >= 5) {
                return suitCards;
            }
        }
        return null;
    }

}


