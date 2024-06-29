package com.pleewson.poker.service;

import com.pleewson.poker.entities.CardsHistory;
import com.pleewson.poker.model.Card;
import com.pleewson.poker.repository.CardsHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardsHistoryService {
    private final CardsHistoryRepository cardsHistoryRepository;

    public CardsHistoryService(CardsHistoryRepository cardsHistoryRepository) {
        this.cardsHistoryRepository = cardsHistoryRepository;
    }

    public void saveCardsHistory(List<Card> player1Cards, List<Card> player2Cards, List<Card> communityCards) {
        CardsHistory cardsHistory = new CardsHistory();
        StringBuilder sbPlayer1Cards = new StringBuilder();
        StringBuilder sbPlayer2Cards = new StringBuilder();
        StringBuilder sbCommunityCards = new StringBuilder();

        for (Card card : player1Cards) { //TODO separate those methods
            sbPlayer1Cards.append("[").append(card.toString()).append("]").append(" ");
        }
        for (Card card : player2Cards) {
            sbPlayer2Cards.append("[").append(card.toString()).append("]").append(" ");
        }
        for (Card card : communityCards) {
            sbCommunityCards.append("[").append(card.toString()).append("]").append(" ");
        }

        cardsHistory.setPlayer1Cards(sbPlayer1Cards.toString());
        cardsHistory.setPlayer2Cards(sbPlayer2Cards.toString());
        cardsHistory.setCommunityCards(sbCommunityCards.toString());
        cardsHistoryRepository.save(cardsHistory);
    }
}
