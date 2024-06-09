package com.pleewson.poker.entities;

import com.pleewson.poker.entities.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String street;
    private String homeNr;
    private String phone;
    private String created;
    private String updated;

    @OneToOne
    private Player player;

}
