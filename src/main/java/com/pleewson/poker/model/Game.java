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
    private int currentPlayer;

    public Game() {
        this.playerList = new ArrayList<>();
        this.gameStarted = false;
        this.currentBet = 0;
        this.communityCards = new ArrayList<>();
        this.currentPlayer = 1;
    }

//    public Game(List<Player> playerList) {
//        this.playerList = playerList;
//    }


    public void addPlayer(Player player){
        playerList.add(player);
    }
    public void startGame() {
        gameStarted = true;
    }
    public void stopGame() {
        gameStarted = false;
    }




}
