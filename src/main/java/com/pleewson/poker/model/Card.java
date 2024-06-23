package com.pleewson.poker.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;

@Getter
@Setter
public class Card {
    private String suit;
    private String rank;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public String toString() {
        return rank + "-" + suit;
    }

    public static final Comparator<Card> RANK_COMPARATOR = new Comparator<Card>() {
        @Override
        public int compare(Card card1, Card card2) {
            return rankToInt(card1.getRank()) - rankToInt(card2.getRank());
        }
    };

    public static int rankToInt(String rank) {
        switch (rank) {
            case "2":
                return 2;
            case "3":
                return 3;
            case "4":
                return 4;
            case "5":
                return 5;
            case "6":
                return 6;
            case "7":
                return 7;
            case "8":
                return 8;
            case "9":
                return 9;
            case "10":
                return 10;
            case "J":
                return 11;
            case "Q":
                return 12;
            case "K":
                return 13;
            case "A":
                return 14;
            default:
                throw new IllegalArgumentException("Unknown rank: " + rank);
        }
    }

}

