package com.pleewson.poker;

import com.pleewson.poker.model.Deck;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {

        Deck deck = new Deck();
        deck.setCards(deck.initializeDeck());

        System.out.println(deck);

        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(1);
        System.out.println(integerList.size());
    }
}
