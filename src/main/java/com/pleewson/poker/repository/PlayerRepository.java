package com.pleewson.poker.repository;

import com.pleewson.poker.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByEmail(String email);
}
