package com.pleewson.poker.repository;

import com.pleewson.poker.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Player findByEmail(String email);

    List<Player> findAllByOrderByTrophiesDesc();

    @Query("SELECT p.trophies FROM Player p WHERE p.id = :playerId")
    int findTrophiesById(@Param("playerId") Long playerId);
}
