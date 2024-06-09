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

    @Email
    private String email;

    private String password;
    private double coins;
    private int trophies;

    @OneToOne(mappedBy = "player")
    private PlayerDetails playerDetails;

    @Transient
    private List<Card> cards;

    @Transient
    private boolean isActive;


}
