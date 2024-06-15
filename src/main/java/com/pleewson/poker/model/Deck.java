package com.pleewson.poker.model;

import com.pleewson.poker.enums.Rank;
import com.pleewson.poker.enums.Suit;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
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

    public List<Card> initializeDeck() {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                cards.add(card);
            }
        }
        return cards;
    }

    private void clearDeck() {
        cards.removeAll(cards);
    }


//    private void clearAllPlayerHands() {
//    }


}
