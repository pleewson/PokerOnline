package com.pleewson.poker.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PairInfo {
    private int higherPairRank;
    private int lowerPairRank;

    public PairInfo(int higherPairRank, int lowerPairRank){
        this.higherPairRank = higherPairRank;
        this.lowerPairRank = lowerPairRank;
    }
}
