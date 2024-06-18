package com.pleewson.poker.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveRequest {
    private int playerNumber;
    private String moveType;

    @Override
    public String toString() {
        return "playerNumber -> " + playerNumber + " moveType -> " + moveType;
    }
}
