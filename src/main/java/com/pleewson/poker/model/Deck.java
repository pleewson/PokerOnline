package com.pleewson.poker.model;

import com.pleewson.poker.entities.Player;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Setter
@Getter
@Component
public class Deck {

    private List<Card> cards;

    private final String[] suits = {"H", "D", "C", "S"};
    private final String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

    public Deck() {
        this.cards = new ArrayList<>();
        cards = initializeDeck();
        shuffleDeck();
    }


    public List<Card> initializeDeck() {
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

    public void dealInitialCards(Player player) {
        player.setCards(drawCards(2));
    }

    public void dealCommunityCards(Game game) {
        switch (game.getRound()) {
            case 1: //skip
                game.setCommunityCards(null);
                break;
            case 2:
                game.setCommunityCards(drawCards(3));
                break;
            case 3:
            case 4:
                List<Card> communityCards = game.getCommunityCards();
                communityCards.add(drawCard());
                game.setCommunityCards(communityCards);
                break;
            default:
                throw new IllegalStateException("Invalid round number");
        }
    }


    private void clearDeck() {
        cards.removeAll(cards);
    }

}
