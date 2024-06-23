package com.pleewson.poker.service;

import com.pleewson.poker.model.Card;

import java.util.*;

public class EvaluateHand {

    public enum HandRank {
        HIGH_CARD,
        PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        STRAIGHT,
        FLUSH,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        STRAIGHT_FLUSH,
        ROYAL_FLUSH
    }

    public static HandRank evaluateHand(List<Card> hand, List<Card> communityCards) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(hand);
        cards.addAll(communityCards);

        boolean flush = isFlush(cards);
        boolean straight = isStraight(cards);
//        boolean royalFlush = false;


        return HandRank.HIGH_CARD;
    }

    public static boolean isFlush(List<Card> cards) {
        //mapping card colors to number of occurrences
        Map<String, Integer> suitCount = new HashMap<>();
        for (Card card : cards) {
            String suit = card.getSuit();
            suitCount.put(suit, suitCount.getOrDefault(suit, 0) + 1);
        }

        //checking if there is at least 5 cards in same color
        for (int count : suitCount.values()) {
            if (count >= 5) {
                return true;
            }
        }
        return false;
    }


    public static boolean isStraight(List<Card> cards) {
        //sprawdzanie czy karty tworzą strita
        Set<Integer> ranks = new HashSet<>();
        for (Card card : cards) {
            ranks.add(Card.rankToInt(card.getRank()));
        }

        if (ranks.size() < 5) {
            return false;
        }

        List<Integer> sortedRanks = new ArrayList<>(ranks);
        Collections.sort(sortedRanks);
        for (int i = 0; i <= sortedRanks.size() - 5; i++) {
            if (sortedRanks.get(i + 4) - sortedRanks.get(i) == 4) {
                return true;
            }
        }

        // Check straight with Ace as low card (A, 2, 3, 4, 5)
        if (ranks.contains(14) && ranks.contains(2) && ranks.contains(3) && ranks.contains(4) && ranks.contains(5)) {
            return true;
        }

        return false;
    }

    public static boolean isRoyalFlush(List<Card> cards) {

        Collections.sort(cards, Card.RANK_COMPARATOR);

        boolean hasTen = false;
        boolean hasJack = false;
        boolean hasQueen = false;
        boolean hasKing = false;
        boolean hasAce = false;

        String suitRoyalFlush = "";

        int aceCount = 0;
        int kingCount = 0;
        int queenCount = 0;

        for (Card card : cards) {
            String rank = card.getRank();
            if (rank.equals("A")) aceCount++;
            if (rank.equals("K")) kingCount++;
            if (rank.equals("Q")) queenCount++;
        }

        //determine color if there will be more than one highest card
        if (isFlush(cards) && isStraight(cards)) {
            if (aceCount == 1) {
                suitRoyalFlush = cards.get(cards.size() - 1).getSuit();
            } else if (kingCount == 1 && aceCount == 2) {
                suitRoyalFlush = cards.get(cards.size() - 3).getSuit();
            } else if (kingCount == 1 && aceCount == 3) {
                suitRoyalFlush = cards.get(cards.size() - 4).getSuit();
            } else if (queenCount == 1 && aceCount == 2 && kingCount == 2) {
                suitRoyalFlush = cards.get(cards.size() - 5).getSuit();
            }
        }

        for (Card card : cards) {
            String rank = card.getRank();
            String suit = card.getSuit();

            if (rank.equals("10") && suit.equals(suitRoyalFlush)) {
                hasTen = true;
            } else if (rank.equals("J") && suit.equals(suitRoyalFlush)) {
                hasJack = true;
            } else if (rank.equals("Q") && suit.equals(suitRoyalFlush)) {
                hasQueen = true;
            } else if (rank.equals("K") && suit.equals(suitRoyalFlush)) {
                hasKing = true;
            } else if (rank.equals("A") && suit.equals(suitRoyalFlush)) {
                hasAce = true;
            }
        }

        if (hasTen && hasJack && hasQueen && hasKing && hasAce && isFlush(cards)) {
            return true;
        }

        return false;
    }

}
