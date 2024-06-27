package com.pleewson.poker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveRequest {

    private int playerNumber;
    private String moveType;
    private Integer betAmount;

    @Override
    public String toString() {
        return "playerNumber -> " + playerNumber + " moveType -> " + moveType;
    }
}
