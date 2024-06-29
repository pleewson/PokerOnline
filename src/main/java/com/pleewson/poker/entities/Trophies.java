package com.pleewson.poker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Trophies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer amount = 0;
    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;

    public void incrementAmount(){
        this.amount++;
    }

}
