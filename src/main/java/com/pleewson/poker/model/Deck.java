package com.pleewson.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
public class Deck {

    private List<Card> cards;

    public Deck() {
        String[] suits = {"H", "D", "C", "S"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        this.cards = new ArrayList<>();
        cards = initializeDeck(suits, ranks);
    }


    public List<Card> initializeDeck(String[] suits, String[] ranks) {
        List<Card> cards = new ArrayList<>();

        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(suit, rank));
            }
        }
        return cards;
    }


    public void shuffleDeck() {
        Collections.shuffle(cards);
    }


    public Card drawCard() {
        return cards.remove(cards.size() - 1);
    }

    public List<Card> drawCards(int numberOfCards) {
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < numberOfCards; i++) {
            drawnCards.add(drawCard());
        }
        return drawnCards;
    }


    private void clearDeck() {
        cards.removeAll(cards);
    }


//    private void clearAllPlayerHands() {
//    public void drawCards(){} game
//    public void drawCards(){} player


}
