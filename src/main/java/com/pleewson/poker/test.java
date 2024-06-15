package com.pleewson.poker;

import com.pleewson.poker.model.Deck;

public class test {
    public static void main(String[] args) {

        Deck deck = new Deck();
        deck.setCards(deck.initializeDeck());

        System.out.println(deck);
    }
}
