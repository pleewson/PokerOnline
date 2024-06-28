package com.pleewson.poker.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    private boolean isAdult;
    private LocalDateTime created;
    private LocalDateTime updated;

    @OneToOne
    private Player player;

}
