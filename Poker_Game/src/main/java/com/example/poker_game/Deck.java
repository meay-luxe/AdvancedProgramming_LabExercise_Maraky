package com.example.poker_game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        buildDeck();
        shuffle();
    }

    private void buildDeck() {
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("No cards left in the deck!");
        }
        return cards.remove(0);
    }

    public int cardsRemaining() {
        return cards.size();
    }

    public void reset() {
        cards.clear();
        buildDeck();
        shuffle();
    }
}