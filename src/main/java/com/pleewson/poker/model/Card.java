package com.pleewson.poker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {
    private Suit suit;
    private Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

}
