package com.pleewson.poker.model;

import com.pleewson.poker.enums.Rank;
import com.pleewson.poker.enums.Suit;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public void shuffleDeck() {
        clearDeck();
        initializeDeck();
    }

//    public void drawCards(){} game
//    public void drawCards(){} player

    private void initializeDeck() {
        clearDeck();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
    }

    private void clearDeck() {
        cards.removeAll(cards);
    }


//    private void clearAllPlayerHands() {
//    }


}
