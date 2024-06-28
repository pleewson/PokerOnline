package com.pleewson.poker.service;

import com.pleewson.poker.entities.Player;
import com.pleewson.poker.model.Card;
import com.pleewson.poker.model.PairInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.*;

@Slf4j
@Service
public class CompareMethods implements CompareMethodsInterface {
    private final GameService gameService;

    public CompareMethods(@Lazy GameService gameService) {
        this.gameService = gameService;
    }


    public void checkTheHighestCardRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());

        //sort reversed to get the highest card first
        player1Cards.sort(Card.RANK_COMPARATOR.reversed());
        player2Cards.sort(Card.RANK_COMPARATOR.reversed());

        for (int i = 0; i < player1Cards.size() && i < player2Cards.size(); i++) {
            int player1CardRank = Card.rankToInt(player1Cards.get(i).getRank());
            int player2CardRank = Card.rankToInt(player2Cards.get(i).getRank());

            if (player1CardRank > player2CardRank) {
                gameService.sendCoinsToPlayer1(player1, player2);
                log.info("{} WIN with {}, {} LOSE with {}", player1.getNickname(), player1CardRank, player2.getNickname(), player2CardRank);
                return;
            } else if (player1CardRank < player2CardRank) {
                gameService.sendCoinsToPlayer2(player1, player2);
                log.info("{} WIN with {}, {} LOSE with {}", player2.getNickname(), player2CardRank, player1.getNickname(), player1CardRank);
                return;
            } else {
                log.info("CHECKING THE HIGHEST CARD");
                gameService.divideCoinsIfDraw(player1, player2);
            }

        }
    }


    public void checkTheHighestPairRank(Player player1, Player player2) {
        List<Card> player1Cards = connectAndSortPlayerCardsWithCommunityCards(player1);
        List<Card> player2Cards = connectAndSortPlayerCardsWithCommunityCards(player2);

        int player1PairRank = findPairRank(player1Cards);
        int player2PairRank = findPairRank(player2Cards);

        if (player1PairRank > player2PairRank) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with pair of {}, {} LOSE with pair of {}", player1.getNickname(), player1PairRank, player2.getNickname(), player2PairRank);
            return;
        } else if (player1PairRank < player2PairRank) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with pair of {}, {} LOSE with pair of {}", player2.getNickname(), player2PairRank, player1.getNickname(), player1PairRank);
            return;
        } else {
            log.info("PAIR VALUE ARE THE SAME. starting checkTheHighestCardRank method...");
            checkTheHighestCardRank(player1, player2);
        }
    }


    public void checkTheHighestTwoPairsRank(Player player1, Player player2) {
        List<Card> player1Cards = connectAndSortPlayerCardsWithCommunityCards(player1);
        List<Card> player2Cards = connectAndSortPlayerCardsWithCommunityCards(player2);

        PairInfo player1PairInfo = findTwoPairs(player1Cards);
        PairInfo player2PairInfo = findTwoPairs(player2Cards);

        if (player1PairInfo.getHigherPairRank() > player2PairInfo.getHigherPairRank()) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                    player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank(),
                    player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank());
        } else if (player1PairInfo.getHigherPairRank() < player2PairInfo.getHigherPairRank()) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                    player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank(),
                    player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank());
        } else {
            if (player1PairInfo.getLowerPairRank() > player2PairInfo.getLowerPairRank()) {
                gameService.sendCoinsToPlayer1(player1, player2);
                log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                        player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank(),
                        player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank());
            } else if (player1PairInfo.getLowerPairRank() < player2PairInfo.getLowerPairRank()) {
                gameService.sendCoinsToPlayer2(player1, player2);
                log.info("{} WIN with two pairs ({} and {}), {} LOSE with two pairs ({} and {})",
                        player2.getNickname(), player2PairInfo.getHigherPairRank(), player2PairInfo.getLowerPairRank(),
                        player1.getNickname(), player1PairInfo.getHigherPairRank(), player1PairInfo.getLowerPairRank());
            } else {
                log.info("BOTH PLAYERS HAVE PAIR");
                checkTheHighestCardRank(player1, player2);
            }
        }

    }


    public void checkTheHighestThreeOfAKindRank(Player player1, Player player2) {
        List<Card> player1Cards = connectAndSortPlayerCardsWithCommunityCards(player1);
        List<Card> player2Cards = connectAndSortPlayerCardsWithCommunityCards(player2);

        int player1ThreeOfAKindRank = findThreeOfAKindRank(player1Cards);
        int player2ThreeOfAKindRank = findThreeOfAKindRank(player2Cards);

        if (player1ThreeOfAKindRank > player2ThreeOfAKindRank) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with three of a kind {}, {} LOSE with three of a kind {}",
                    player1.getNickname(), player1ThreeOfAKindRank, player2.getNickname(), player2ThreeOfAKindRank);
        } else if (player1ThreeOfAKindRank < player2ThreeOfAKindRank) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with three of a kind {}, {} LOSE with three of a kind {}",
                    player2.getNickname(), player2ThreeOfAKindRank, player1.getNickname(), player1ThreeOfAKindRank);
        } else {
            //remove three of a kind to compare kickers
            removeCardsWithRank(player1Cards, player1ThreeOfAKindRank);
            removeCardsWithRank(player2Cards, player2ThreeOfAKindRank);

            checkTheHighestKicker(player1, player1Cards, player2, player2Cards);
        }
    }


    public void checkTheHighestStraightRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(gameService.getGame().getCommunityCards());
        player2Cards.addAll(gameService.getGame().getCommunityCards());

        int player1HighestStraightCard = findHighestStraightCard(player1Cards);
        int player2HighestStraightCard = findHighestStraightCard(player2Cards);

        if (player1HighestStraightCard > player2HighestStraightCard) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with straight {}, {} LOSE with straight {}",
                    player1.getNickname(), player1HighestStraightCard, player2.getNickname(), player2HighestStraightCard);
        } else if (player1HighestStraightCard < player2HighestStraightCard) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with straight {}, {} LOSE with straight {}",
                    player2.getNickname(), player2HighestStraightCard, player1.getNickname(), player1HighestStraightCard);
        } else {
            log.info("DRAW. BOTH PLAYERS HAVE SAME HIGHEST STRAIGHT CARD {} {} , {} {}", player1.getNickname(), player1HighestStraightCard, player2.getNickname(), player2HighestStraightCard);
            gameService.divideCoinsIfDraw(player1, player2);
        }
    }


    public void checkTheHighestFlushRank(Player player1, Player player2) {
        List<Card> player1Cards = connectAndSortPlayerCardsWithCommunityCards(player1);
        List<Card> player2Cards = connectAndSortPlayerCardsWithCommunityCards(player2);

        //flush cards
        List<Card> player1FlushCards = findFlushCards(player1Cards);
        List<Card> player2FlushCards = findFlushCards(player2Cards);

        // Find the highest card in the flush for each player
        int player1FlushHighCard = findHighestCardInFlush(player1FlushCards);
        int player2FlushHighCard = findHighestCardInFlush(player2FlushCards);

        if (player1FlushHighCard > player2FlushHighCard) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with flush {}, {} LOSE with flush {}",
                    player1.getNickname(), player1FlushHighCard, player2.getNickname(), player2FlushHighCard);
        } else if (player1FlushHighCard < player2FlushHighCard) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with flush {}, {} LOSE with flush {}",
                    player2.getNickname(), player2FlushHighCard, player1.getNickname(), player1FlushHighCard);
        } else {
            //remove flush cards to compare kicker
            player1Cards.removeAll(player1FlushCards);  //TODO FIX ALL removeAll Methods (it is deleting too much cards)
            player2Cards.removeAll(player2FlushCards);

            checkTheHighestKicker(player1, player1Cards, player2, player2Cards);
        }
    }


    public void checkTheHighestFullHouseRank(Player player1, Player player2) {
        List<Card> player1Cards = new ArrayList<>(player1.getCards());
        List<Card> player2Cards = new ArrayList<>(player2.getCards());
        player1Cards.addAll(gameService.getGame().getCommunityCards());
        player2Cards.addAll(gameService.getGame().getCommunityCards());

        //sort reversed to get the highest card first
        player1Cards.sort(Card.RANK_COMPARATOR.reversed());
        player2Cards.sort(Card.RANK_COMPARATOR.reversed());

        int player1TripleRank = findThreeOfAKindRank(player1Cards);
        int player2TripleRank = findThreeOfAKindRank(player2Cards);
        int player1PairRank = findPairRank(player1Cards);
        int player2PairRank = findPairRank(player2Cards);

        if (player1TripleRank > player2TripleRank) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with full house ({} and {}), {} LOSE with full house ({} and {})",
                    player1.getNickname(), player1TripleRank, player1PairRank, player2.getNickname(), player2TripleRank, player2PairRank);
        } else if (player1TripleRank < player2TripleRank) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with full house ({} and {}), {} LOSE with full house ({} and {})",
                    player2.getNickname(), player2TripleRank, player2PairRank, player1.getNickname(), player1TripleRank, player1PairRank);
        } else if (player1PairRank > player2PairRank) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with full house ({} and {}), {} LOSE with full house ({} and {})",
                    player1.getNickname(), player1TripleRank, player1PairRank, player2.getNickname(), player2TripleRank, player2PairRank);
        } else if (player1PairRank < player2PairRank) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with full house ({} and {}), {} LOSE with full house ({} and {})",
                    player2.getNickname(), player2TripleRank, player2PairRank, player1.getNickname(), player1TripleRank, player1PairRank);
        } else {
            log.info("DRAW. Both triple and pair ranks are the same. {} full house rank ({} - {}),  {} full house rank ({} - {})",
                    player1.getNickname(), player1TripleRank, player1PairRank, player2.getNickname(), player2TripleRank, player2PairRank);
            gameService.divideCoinsIfDraw(player1, player2);
        }
    }


    public void checkTheHighestFourOfAKindRank(Player player1, Player player2) {
        List<Card> player1Cards = connectAndSortPlayerCardsWithCommunityCards(player1);
        List<Card> player2Cards = connectAndSortPlayerCardsWithCommunityCards(player2);

        int player1FourOfAKindRank = findFourOfAKindRank(player1Cards);
        int player2FourOfAKindRank = findFourOfAKindRank(player2Cards);

        if (player1FourOfAKindRank > player2FourOfAKindRank) {
            gameService.sendCoinsToPlayer1(player1, player2);
            log.info("{} WIN with four of a kind {}, {} LOSE with four of a kind {}",
                    player1.getNickname(), player1FourOfAKindRank, player2.getNickname(), player2FourOfAKindRank);
        } else if (player1FourOfAKindRank < player2FourOfAKindRank) {
            gameService.sendCoinsToPlayer2(player1, player2);
            log.info("{} WIN with four of a kind {}, {} LOSE with four of a kind {}",
                    player2.getNickname(), player2FourOfAKindRank, player1.getNickname(), player1FourOfAKindRank);
        } else {
            //remove four of a kind cards to compare kicker
            removeCardsWithRank(player1Cards, player1FourOfAKindRank);
            removeCardsWithRank(player2Cards, player2FourOfAKindRank);
            checkTheHighestKicker(player1, player1Cards, player2, player2Cards);
        }
    }


    public boolean areBothTwoHighCards(Player player1, Player player2) {
        return player1.getHandRank() == 1 && player2.getHandRank() == 1;
    }
//AAA 77
    public boolean areBothPair(Player player1, Player player2) {
        return player1.getHandRank() == 2 && player2.getHandRank() == 2;
    }

    public boolean areBothTwoPairs(Player player1, Player player2) {
        return player1.getHandRank() == 3 && player2.getHandRank() == 3;
    }

    public boolean areBothThreeOfAKind(Player player1, Player player2) {
        return player1.getHandRank() == 4 && player2.getHandRank() == 4;
    }

    public boolean areBothStraight(Player player1, Player player2) {
        return player1.getHandRank() == 5 && player2.getHandRank() == 5;
    }

    public boolean areBothFlush(Player player1, Player player2) {
        return player1.getHandRank() == 6 && player2.getHandRank() == 6;
    }

    public boolean areBothFullHouse(Player player1, Player player2) {
        return player1.getHandRank() == 7 && player2.getHandRank() == 7;
    }

    public boolean areBothFourOfAKind(Player player1, Player player2) {
        return player1.getHandRank() == 8 && player2.getHandRank() == 8;
    }

    public boolean areBothStraightFlush(Player player1, Player player2) {
        return player1.getHandRank() == 9 && player2.getHandRank() == 9;
    }

    public boolean areBothRoyalFlush(Player player1, Player player2) {
        return player1.getHandRank() == 10 && player2.getHandRank() == 10;
    }


    //AUXILIARY METHODS
    public void removeCardsWithRank(List<Card> cards, int rank) {
        cards.removeIf(card -> Card.rankToInt(card.getRank()) == rank);
    }


    public int findPairRank(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 2) {
                return Card.rankToInt(entry.getKey());
            }
        }
        return -1;
    }


    public int findThreeOfAKindRank(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 3) {
                return Card.rankToInt(entry.getKey());
            }
        }
        return -1;
    }


    public int findFourOfAKindRank(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 4) {
                return Card.rankToInt(entry.getKey());
            }
        }
        return -1;
    }


    public int findHighestStraightCard(List<Card> cards) { //TODO +/rank/
        Set<Integer> ranks = new HashSet<>();
        for (Card card : cards) {
            ranks.add(Card.rankToInt(card.getRank()));
        }

        List<Integer> sortedRanks = new ArrayList<>(ranks);
        Collections.sort(sortedRanks);
        for (int i = 0; i <= sortedRanks.size() - 5; i++) {
            if (sortedRanks.get(i + 4) - sortedRanks.get(i) == 4) {
                return sortedRanks.get(i);
            }
        }
        // return highest rank when -> Ace as low card (A, 2, 3, 4, 5)
        if (ranks.contains(14) && ranks.contains(2) && ranks.contains(3) && ranks.contains(4) && ranks.contains(5)) {
            return 5;
        }
        return -1;
    }


    private int findHighestCardInFlush(List<Card> cards) {
        return Card.rankToInt(Collections.max(cards, Card.RANK_COMPARATOR).getRank());
    }


    private PairInfo findTwoPairs(List<Card> cards) {
        Map<String, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            rankCount.put(card.getRank(), rankCount.getOrDefault(card.getRank(), 0) + 1);
        }

        int higherPairRank = 0;
        int lowerPairRank = 0;

        for (Map.Entry<String, Integer> entry : rankCount.entrySet()) {
            if (entry.getValue() == 2) {
                if (higherPairRank == 0) {
                    higherPairRank = Card.rankToInt(entry.getKey());
                } else {
                    lowerPairRank = Card.rankToInt(entry.getKey());
                }
            }
        }
        return new PairInfo(higherPairRank, lowerPairRank);
    }


    public void checkTheHighestKicker(Player player1, List<Card> player1Cards, Player player2, List<Card> player2Cards) {
        player1Cards.sort(Card.RANK_COMPARATOR.reversed());
        player2Cards.sort(Card.RANK_COMPARATOR.reversed());

        for (int i = 0; i < player1Cards.size(); i++) {
            int player1KickerRank = Card.rankToInt(player1Cards.get(i).getRank());
            int player2KickerRank = Card.rankToInt(player2Cards.get(i).getRank());

            if (player1KickerRank > player2KickerRank) {
                gameService.sendCoinsToPlayer1(player1, player2);
                log.info("{} WIN with kicker {}, {} LOSE with kicker {}",
                        player1.getNickname(), player1KickerRank, player2.getNickname(), player2KickerRank);
                return;
            } else if (player1KickerRank < player2KickerRank) {
                gameService.sendCoinsToPlayer2(player1, player2);
                log.info("{} WIN with kicker {}, {} LOSE with kicker {}",
                        player2.getNickname(), player2KickerRank, player1.getNickname(), player1KickerRank);
                return;
            }
        }
        gameService.divideCoinsIfDraw(player1, player2);
    }


    private List<Card> findFlushCards(List<Card> cards) {
        Map<String, List<Card>> suitMap = new HashMap<>();

        for (Card card : cards) {
            suitMap.computeIfAbsent(card.getSuit(), k -> new ArrayList<>()).add(card);
        }

        for (List<Card> suitCards : suitMap.values()) {
            if (suitCards.size() >= 5) {
                return suitCards;
            }
        }
        return Collections.emptyList();
    }


    private List<Card> connectAndSortPlayerCardsWithCommunityCards(Player player) {
        List<Card> playerCards = new ArrayList<>(player.getCards());
        playerCards.addAll(gameService.getGame().getCommunityCards());
        playerCards.sort(Card.RANK_COMPARATOR.reversed());

        return playerCards;
    }

}



