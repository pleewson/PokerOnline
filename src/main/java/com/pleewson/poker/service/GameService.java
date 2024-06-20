package com.pleewson.poker.service;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Deck;
import com.pleewson.poker.model.Game;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    //    public void nextPlayer(){};
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


    //    public void makeMove(){};
    public void startGame() {
        game.setGameStarted(true);
    }

    public void stopGame() {
        game.setGameStarted(false);
    }


    public void nextPlayer() {
        if (game.getCurrentPlayer() == 1) {
            game.setCurrentPlayer(2);
        } else {
            game.setCurrentPlayer(1);
        }
    }


    public void processMove(Game game, Player player, String moveType) {
        switch (moveType) {
            case "bet": {
                log.info("playerNum {} moveType -> {} ", player.getPlayerNumber(), moveType);
                game.setCurrentBet((game.getCurrentBet()) + 20); //TEST VALUE
                player.setCoins(player.getCoins() - 20);
                break;
            }
            case "check": {
                log.info("playerNum {} moveType -> {} ", player.getPlayerNumber(), moveType);
                game.setCurrentBet((game.getCurrentBet()) + 1);  //TEST VALUE
                break;
            }
            case "fold": {
                //player surrender
                //send tableBet to winner
                //set player.currentBet = 0
                //set tableBet = 0;
                //next round, next player Starts round

                log.info("playerNum {} moveType -> {} ", player.getPlayerNumber(), moveType);
                game.setCurrentBet((game.getCurrentBet()) - 20); //TEST VALUE
                break;
            }
            default:
                throw new IllegalArgumentException("Invalid move type");
        }
    }


    public Player determineWinner(Game game){
        return null;
    }
    //TODO^


    //public void raiseBet(Player player, int amount){}
    //sprawdz czy gracz ma wystarczajaca ilosc zetonow aby podniesc zaklad,
    //jesli ma zabierz mu zetony i dodaj do currentBet
    //jesli nie ma wyswietl komunikat o braku zetonow

    //public void checkBet(Player player(){}
    //jesli gracz sprawdza zaklad, nic robi nic

//    public void fold(Player player){}
    //obsluga passowania przez gracza
    //mozna go zdezaktywowac na dana ture
}
