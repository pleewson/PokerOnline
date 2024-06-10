package com.pleewson.poker.repository;

import com.pleewson.poker.entities.PlayerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerDetailsRepository extends JpaRepository<PlayerDetails, Long> {
}
