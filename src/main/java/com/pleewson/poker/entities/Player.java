package com.pleewson.poker.entities;

import com.pleewson.poker.model.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Player {

    public Player() {
        this.cards = new ArrayList<>();
        this.isActive = true;
        this.currentBet = 0;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Email
    private String email;
    private String password;
    private int trophies = 0;

    @OneToOne(mappedBy = "player")
    private PlayerDetails playerDetails;

    @Transient
    private Integer coins;

    @Transient
    private Integer currentBet;

    @Transient
    private List<Card> cards;

    @Transient
    private boolean isActive;

    @Transient
    private int playerNumber;

    @Transient
    private boolean check;

    @Transient
    private Integer handRank;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(id, player.id);
    }

}
