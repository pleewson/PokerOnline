package com.pleewson.poker.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String password;

    @Column(columnDefinition = "tinyint(1) default 0")
    private boolean inGame;

    @Email
    private String email;
    private BigDecimal balance;

    @OneToOne
    private PlayerDetails playerDetails;

    @ManyToOne
    private Table table;

    @ManyToMany
    @JoinTable(
            name = "player_bets",
            joinColumns = @JoinColumn(name = "player_id"),
            inverseJoinColumns = @JoinColumn(name = "bet_id"))
    private List<Bet> bets = new ArrayList<>();

}
