package com.pleewson.poker;

import com.pleewson.poker.model.Card;
import com.pleewson.poker.model.Deck;
import com.pleewson.poker.service.EvaluateHand;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class test {
    public static void main(String[] args) {

        EvaluateHand evaluateHand = new EvaluateHand();

//        String[] suits = {"H", "D", "C", "S"};
//        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        //COLOR
        List<Card> manualHand_COLOR = new ArrayList<>();
        manualHand_COLOR.add(new Card("D", "6"));
        manualHand_COLOR.add(new Card("H", "7"));
        manualHand_COLOR.add(new Card("C", "8"));
        manualHand_COLOR.add(new Card("D", "Q"));
        manualHand_COLOR.add(new Card("D", "A"));

        manualHand_COLOR.add(new Card("D", "J"));
        manualHand_COLOR.add(new Card("D", "10"));
        System.out.println("is there color? : " + evaluateHand.isFlush(manualHand_COLOR));


        //STRAIGHT
        List<Card> manualHand_STRAIGHT = new ArrayList<>();
        manualHand_STRAIGHT.add(new Card("D", "3"));
        manualHand_STRAIGHT.add(new Card("H", "9"));
        manualHand_STRAIGHT.add(new Card("D", "7"));
        manualHand_STRAIGHT.add(new Card("S", "8"));
        manualHand_STRAIGHT.add(new Card("S", "J"));

        manualHand_STRAIGHT.add(new Card("C", "10"));
        manualHand_STRAIGHT.add(new Card("C", "A"));
        System.out.println("is there straight? : " + evaluateHand.isStraight(manualHand_STRAIGHT));


        //ROYAL_FLUSH
        List<Card> manualHand_ROYAL_FLUSH = new ArrayList<>();
        manualHand_ROYAL_FLUSH.add(new Card("C", "A"));
        manualHand_ROYAL_FLUSH.add(new Card("D", "J"));
        manualHand_ROYAL_FLUSH.add(new Card("D", "Q"));
        manualHand_ROYAL_FLUSH.add(new Card("D", "K"));
        manualHand_ROYAL_FLUSH.add(new Card("C", "K"));

        manualHand_ROYAL_FLUSH.add(new Card("D", "10"));
        manualHand_ROYAL_FLUSH.add(new Card("D", "A"));
        System.out.println("is there royal flush? : " + evaluateHand.isRoyalFlush(manualHand_ROYAL_FLUSH));


        //FOUR OF A KIND
        List<Card> manualHand_FOUR_OF_A_KIND = new ArrayList<>();
        manualHand_FOUR_OF_A_KIND.add(new Card("C", "5"));
        manualHand_FOUR_OF_A_KIND.add(new Card("H", "7"));
        manualHand_FOUR_OF_A_KIND.add(new Card("H", "7"));
        manualHand_FOUR_OF_A_KIND.add(new Card("S", "A"));
        manualHand_FOUR_OF_A_KIND.add(new Card("S", "7"));

        manualHand_FOUR_OF_A_KIND.add(new Card("D", "10"));
        manualHand_FOUR_OF_A_KIND.add(new Card("D", "7"));
        System.out.println("is there royal four of a kind? : " + evaluateHand.isFourOfAKind(manualHand_FOUR_OF_A_KIND));


        //FULL HOUSE
        List<Card> manualHand_FULL_HOUSE = new ArrayList<>();
        manualHand_FULL_HOUSE.add(new Card("C", "5"));
        manualHand_FULL_HOUSE.add(new Card("H", "7"));
        manualHand_FULL_HOUSE.add(new Card("S", "9"));
        manualHand_FULL_HOUSE.add(new Card("S", "5"));
        manualHand_FULL_HOUSE.add(new Card("S", "A"));

        manualHand_FULL_HOUSE.add(new Card("D", "A"));
        manualHand_FULL_HOUSE.add(new Card("D", "5"));
        System.out.println("is there full house? : " + evaluateHand.isFullHouse(manualHand_FULL_HOUSE));


        //THREE OF A KIND
        List<Card> manualHand_THREE_OF_A_KIND = new ArrayList<>();
        manualHand_THREE_OF_A_KIND.add(new Card("C", "9"));
        manualHand_THREE_OF_A_KIND.add(new Card("C", "4"));
        manualHand_THREE_OF_A_KIND.add(new Card("S", "8"));
        manualHand_THREE_OF_A_KIND.add(new Card("C", "A"));
        manualHand_THREE_OF_A_KIND.add(new Card("D", "5"));

        manualHand_THREE_OF_A_KIND.add(new Card("S", "9"));
        manualHand_THREE_OF_A_KIND.add(new Card("S", "9"));
        System.out.println("is there THREE OF A  KIND? : " + evaluateHand.isThreeOfAKind(manualHand_THREE_OF_A_KIND));


        //TWO PAIRS
        List<Card> manualHand_TWO_PAIR = new ArrayList<>();
        manualHand_TWO_PAIR.add(new Card("C", "9"));
        manualHand_TWO_PAIR.add(new Card("C", "5"));
        manualHand_TWO_PAIR.add(new Card("S", "2"));
        manualHand_TWO_PAIR.add(new Card("S", "4"));
        manualHand_TWO_PAIR.add(new Card("D", "9"));

        manualHand_TWO_PAIR.add(new Card("D", "5"));
        manualHand_TWO_PAIR.add(new Card("S", "Q"));
        System.out.println("is there TWO PAIR? : " + evaluateHand.isTwoPair(manualHand_TWO_PAIR));


        //PAIR
        List<Card> manualHand_PAIR = new ArrayList<>();
        manualHand_PAIR.add(new Card("S", "9"));
        manualHand_PAIR.add(new Card("S", "5"));
        manualHand_PAIR.add(new Card("S", "10"));
        manualHand_PAIR.add(new Card("D", "Q"));
        manualHand_PAIR.add(new Card("D", "A"));

        manualHand_PAIR.add(new Card("H", "K"));
        manualHand_PAIR.add(new Card("H", "A"));
        System.out.println("is there PAIR? : " + evaluateHand.isPair(manualHand_PAIR));



//        List<Integer> integerList = new ArrayList<>();
//        integerList.add(5);
//        integerList.add(6);
//        integerList.add(7);
//        integerList.add(5);
//        integerList.add(8);
//
//        List<Integer> dwadwa = new ArrayList<>();
//        dwadwa.add(5);
//        integerList.removeAll(dwadwa);
//
//        System.out.println(integerList + " LSLSLSLSLLS");

        System.out.println(BCrypt.hashpw("admin", BCrypt.gensalt()));

        List<Integer> integerList = new ArrayList<>();

        //[9-S, 6-H, 3-S, K-H, 9-C]
       //K-C, 5-C]
       //[8-H, A-H]

        List<Card> stol = new ArrayList<>();
        stol.add(new Card("S", "3"));
        stol.add(new Card("S", "2"));
        stol.add(new Card("H", "8"));
        stol.add(new Card("H", "K"));
        stol.add(new Card("C", "Q"));

        List<Card> gracz1 = new ArrayList<>();

        gracz1.add(new Card("S", "8"));
        gracz1.add(new Card("S", "8"));

        List<Card> gracz2 = new ArrayList<>();
        gracz2.add(new Card("H", "2"));
        gracz2.add(new Card("C", "4"));

//        System.out.println("is there PAIR? : " + evaluateHand.isPair(manualHand_PAIR));
        System.out.println("TUTAJ TEST   " + EvaluateHand.evaluateHand(gracz1,stol).getRankValue());
        System.out.println("TUTAJ TEST   " + EvaluateHand.evaluateHand(gracz2,stol).getRankValue());



        }
}
