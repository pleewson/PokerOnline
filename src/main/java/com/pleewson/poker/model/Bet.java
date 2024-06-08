package com.pleewson.poker.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal amount;

//    private Hand handId;

    private LocalDateTime dateTime;
    
    @ManyToMany(mappedBy = "bets")
    private Player playerId;

}
