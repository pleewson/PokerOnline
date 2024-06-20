package com.pleewson.poker.model;

import lombok.Getter;
import lombok.Setter;

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
}
