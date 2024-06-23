package com.pleewson.poker;

import com.pleewson.poker.model.Card;
import com.pleewson.poker.model.Deck;
import com.pleewson.poker.service.EvaluateHand;

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
        List<Card> manualHand_RF = new ArrayList<>();
        manualHand_RF.add(new Card("C", "A"));
        manualHand_RF.add(new Card("D", "J"));
        manualHand_RF.add(new Card("D", "Q"));
        manualHand_RF.add(new Card("D", "K"));
        manualHand_RF.add(new Card("C", "K"));

        manualHand_RF.add(new Card("D", "10"));
        manualHand_RF.add(new Card("D", "A"));
        System.out.println("is there royal flush? : " + evaluateHand.isRoyalFlush(manualHand_RF));



    }
}
