package com.example.poker_game;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Deck deck;
    private Player player;
    private Player computer;
    private List<Card> communityCards;
    private GameState state;

    public enum GameState {
        WAITING,
        DEALT,
        TURN,
        RIVER,
        SHOWDOWN
    }

    public Game() {
        deck = new Deck();
        player = new Player("Player");
        computer = new Player("Computer");
        communityCards = new ArrayList<>();
        state = GameState.WAITING;
    }

    public void deal() {

        deck.reset();
        player.clearHand();
        computer.clearHand();
        communityCards.clear();

        player.addCard(deck.deal());
        computer.addCard(deck.deal());
        player.addCard(deck.deal());
        computer.addCard(deck.deal());

        state = GameState.DEALT;
    }

    public void flop() {
        if (state != GameState.DEALT) {
            throw new IllegalStateException("Cannot flop now!");
        }
         deck.deal(); // burn
        communityCards.add(deck.deal());
        communityCards.add(deck.deal());
        communityCards.add(deck.deal());

        state = GameState.FLOP;
    }

    public void turn() {
        if (state != GameState.FLOP) {
            throw new IllegalStateException("Cannot turn now!");
        }
        deck.deal(); // burn
        communityCards.add(deck.deal());

        state = GameState.TURN;
    }

    public void river() {
        if (state != GameState.TURN) {
            throw new IllegalStateException("Cannot river now!");
        }
       deck.deal(); // burn
        communityCards.add(deck.deal());

        state = GameState.RIVER;
        state = GameState.SHOWDOWN;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getComputer() {
        return computer;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public GameState getState() {
        return state;
    }
}