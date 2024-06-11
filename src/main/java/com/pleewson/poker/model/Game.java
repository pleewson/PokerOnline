package com.pleewson.poker.model;

import com.pleewson.poker.entities.Player;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Game {

    private List<Player> playerList;
    private boolean gameStarted;
    private List<Card> communityCards;
    private int currentBet;

    public Game() {
        this.playerList = new ArrayList<>();
        this.gameStarted = false;
        this.currentBet = 0;
    }

    public Game(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }

    public void startGame() {
        gameStarted = true;
    }
    public void stopGame() {
        gameStarted = false;
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
