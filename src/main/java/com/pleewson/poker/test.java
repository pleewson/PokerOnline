package com.pleewson.poker;

import com.pleewson.poker.model.Card;
import com.pleewson.poker.model.Deck;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {

        Deck deck = new Deck();
        for(Card card : deck.getCards()){
            System.out.println(card);
        }
    }
}
