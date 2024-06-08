package com.pleewson.poker.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PlayerDetails {
    @Id
    private Long playerId;
    private String firstName;
    private String lastName;
    private String country;
    private String city;
    private String street;
    private String homeNr;
    private String phone;
    private String created;
    private String updated;

}
