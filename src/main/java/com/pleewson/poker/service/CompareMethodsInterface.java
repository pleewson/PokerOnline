package com.pleewson.poker.service;

import com.pleewson.poker.entities.Player;

public interface CompareMethodsInterface {

    void checkTheHighestCardRank(Player player1, Player player2);
    void checkTheHighestPairRank (Player player1, Player player2);
    void checkTheHighestTwoPairsRank (Player player1, Player player2);
    void checkTheHighestThreeOfAKindRank (Player player1, Player player2);
    void checkTheHighestStraightRank (Player player1, Player player2);
    void checkTheHighestFlushRank (Player player1, Player player2);
    void checkTheHighestFullHouseRank (Player player1, Player player2);
    void checkTheHighestFourOfAKindRank (Player player1, Player player2);


    boolean areBothTwoHighCards(Player player1, Player player2);
    boolean areBothPair(Player player1, Player player2);
    boolean areBothTwoPairs(Player player1, Player player2);
    boolean areBothThreeOfAKind(Player player1, Player player2);
    boolean areBothStraight(Player player1, Player player2);
    boolean areBothFlush(Player player1, Player player2);
    boolean areBothFullHouse(Player player1, Player player2);
    boolean areBothFourOfAKind(Player player1, Player player2);
    boolean areBothStraightFlush(Player player1, Player player2);
    boolean areBothRoyalFlush(Player player1, Player player2);

}
