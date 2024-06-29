package com.pleewson.poker.repository;

import com.pleewson.poker.entities.CardsHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardsHistoryRepository extends JpaRepository<CardsHistory, Long> {
}
