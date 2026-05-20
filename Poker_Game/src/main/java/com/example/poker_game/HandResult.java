package com.example.poker_game;

import java.util.List;

public class HandResult implements Comparable<HandResult> {
    private final HandRank handRank;
    private final List<Integer> tiebreakers;

    public HandResult(HandRank handRank, List<Integer> tiebreakers) {
        this.handRank = handRank;
        this.tiebreakers = tiebreakers;
    }

    public HandRank getHandRank() {
        return handRank;
    }

    public List<Integer> getTiebreakers() {
        return tiebreakers;
    }

    @Override
    public int compareTo(HandResult other) {
        // First compare hand rank (e.g., Flush beats Straight)
        if (this.handRank.getPower() != other.handRank.getPower()) {
            return Integer.compare(this.handRank.getPower(), other.handRank.getPower());
        }

        for (int i = 0; i < Math.min(tiebreakers.size(), other.tiebreakers.size()); i++) {
            int cmp = Integer.compare(tiebreakers.get(i), other.tiebreakers.get(i));
            if (cmp != 0) {
                return cmp;
            }
        }


        return 0;
    }

    @Override
    public String toString() {
        return handRank.getDisplay();
    }
}