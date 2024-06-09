package com.pleewson.poker.model;

import com.pleewson.poker.enums.Rank;
import com.pleewson.poker.enums.Suit;
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
