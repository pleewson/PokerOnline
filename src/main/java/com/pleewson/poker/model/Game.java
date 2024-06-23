package com.pleewson.poker.model;

import com.pleewson.poker.entities.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class Game {

    private List<Player> playerList;
    private boolean gameStarted;
    private List<Card> communityCards;
    private int currentBet;
    private int currentPlayer;
    private int round = 1;

    public Game() {
        this.playerList = new ArrayList<>();
        this.gameStarted = false;
        this.currentBet = 0;
        this.communityCards = new ArrayList<>();
        this.currentPlayer = 1;
    }

    public List<Card> getCommunityCards() {
        if (this.communityCards == null) {
            this.communityCards = new ArrayList<>();
        }
        return this.communityCards;
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


}
