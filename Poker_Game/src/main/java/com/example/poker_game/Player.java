package com.example.poker_game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Card> holeCards;

    public Player(String name) {
        this.name = name;
        this.holeCards = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHoleCards() {
        return holeCards;
    }

    public void addCard(Card card) {
        if (holeCards.size() >= 2) {
            throw new IllegalStateException(name + " already has 2 cards!");
        }
        holeCards.add(card);
    }

    public void clearHand() {
        holeCards.clear();
    }
}