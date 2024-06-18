package com.pleewson.poker.entities;

import com.pleewson.poker.model.Card;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Player {

    public Player() {
        this.cards = new ArrayList<>();
        this.isActive = true;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Email
    private String email;
    private String password;
    private double coins = 0;
    private int trophies = 0;

    @OneToOne(mappedBy = "player")
    private PlayerDetails playerDetails;

    @Transient
    private List<Card> cards;

    @Transient
    private boolean isActive = true;

    @Transient
    private int playerNumber;



}
