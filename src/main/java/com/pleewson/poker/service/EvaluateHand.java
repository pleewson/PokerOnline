package com.pleewson.poker.service;

import com.pleewson.poker.enums.HandRankEnum;
import com.pleewson.poker.model.Card;

import java.util.*;

public class EvaluateHand {

    public static HandRankEnum evaluateHand(List<Card> hand, List<Card> communityCards) {
        List<Card> cards = new ArrayList<>();
        cards.addAll(hand);
        cards.addAll(communityCards);

        if (isRoyalFlush(cards)) {
            return HandRankEnum.ROYAL_FLUSH; //10
        } else if (isStraight(cards) && isFlush(cards)) { // TODO - IMPROVE METHOD
            return HandRankEnum.STRAIGHT_FLUSH; //9
        } else if (isFourOfAKind(cards)) {
            return HandRankEnum.FOUR_OF_A_KIND; //8
        } else if (isFullHouse(cards)) {
            return HandRankEnum.FULL_HOUSE; //7
        } else if (isFlush(cards)) {
            return HandRankEnum.FLUSH; //6
        } else if (isStraight(cards)) {
            return HandRankEnum.STRAIGHT; //5
        } else if (isThreeOfAKind(cards)) {
            return HandRankEnum.THREE_OF_A_KIND; //4
        } else if (isTwoPair(cards)) {
            return HandRankEnum.TWO_PAIR; //3
        } else if (isPair(cards)) {
            return HandRankEnum.PAIR; //2
        } else {
            return HandRankEnum.HIGH_CARD; //1
        }
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

        cards.sort(Card.RANK_COMPARATOR);

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


    public static boolean isFourOfAKind(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        for (int count : rankCount.values()) {
            if (count == 4) {
                return true;
            }
        }
        return false;
    }


    public static boolean isFullHouse(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        boolean hasThreeOfaKind = false;
        boolean hasPair = false;

        for (int count : rankCount.values()) {
            if (count == 3) {
                hasThreeOfaKind = true;
            } else if (count == 2) {
                hasPair = true;
            }
        }

        return hasThreeOfaKind && hasPair;
    }


    public static boolean isThreeOfAKind(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        for (int count : rankCount.values()) {
            if (count == 3) {
                return true;
            }
        }
        return false;
    }


    public static boolean isTwoPair(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        int pairCount = 0;
        for (int count : rankCount.values()) {
            if (count == 2) {
                pairCount++;
            }
        }
        return pairCount >= 2;
    }


    public static boolean isPair(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        for (int count : rankCount.values()) {
            if (count == 2) {
                return true;
            }
        }
        return false;
    }

}

