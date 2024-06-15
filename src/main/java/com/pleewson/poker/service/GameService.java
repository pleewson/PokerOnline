package com.pleewson.poker.service;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Game;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
public class GameService {
    private Game game;

    public Game createNewGame() {
        game = new Game();
        return game;
    }

    //    public void nextPlayer(){};
    public void addPlayer(Player player) {
        if (game.isGameStarted()) {
            throw new IllegalStateException("Game has already started.");
        }

//        if (game.getPlayerList().size() >= 2) {
//            throw new IllegalStateException("Game is full.");
//        }

        game.addPlayer(player);
    }



    //    public void makeMove(){};
    public void startGame() {
        game.setGameStarted(true);
    }

    public void stopGame() {
        game.setGameStarted(false);
    }


    //nextPlayerMove()

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
