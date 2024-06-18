package com.pleewson.poker.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoveRequest {
    private Long playerId;
    private String moveType;
}
