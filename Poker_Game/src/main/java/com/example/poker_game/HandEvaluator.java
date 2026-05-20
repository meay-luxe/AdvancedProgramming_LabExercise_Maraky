package com.example.poker_game;

import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {

    /**
     * Evaluate the best 5-card hand from 7 cards (2 hole + 5 community)
     */
    public static HandResult evaluate(List<Card> holeCards, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(holeCards);
        allCards.addAll(communityCards);

        // Generate all combinations of 5 cards from 7
        List<List<Card>> combinations = getCombinations(allCards, 5);

        // Evaluate each combination and return the best one
        HandResult best = null;
        for (List<Card> combo : combinations) {
            HandResult result = evaluateFive(combo);
            if (best == null || result.compareTo(best) > 0) {
                best = result;
            }
        }

        return best;
    }

    /**
     * Evaluate exactly 5 cards
     */
    private static HandResult evaluateFive(List<Card> cards) {
        // Sort cards by rank value descending
        cards.sort((a, b) -> Integer.compare(b.getRank().getValue(), a.getRank().getValue()));

        boolean flush = isFlush(cards);
        boolean straight = isStraight(cards);

        // Get rank counts for pairs, trips, etc.
        Map<Integer, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            int val = card.getRank().getValue();
            rankCount.put(val, rankCount.getOrDefault(val, 0) + 1);
        }

        // Sort ranks by count (descending), then by value (descending)
        List<Map.Entry<Integer, Integer>> sorted = rankCount.entrySet().stream()
                .sorted((a, b) -> {
                    int cmp = Integer.compare(b.getValue(), a.getValue());
                    if (cmp != 0) return cmp;
                    return Integer.compare(b.getKey(), a.getKey());
                })
                .collect(Collectors.toList());

        // Build tiebreaker list (ordered by importance)
        List<Integer> tiebreakers = sorted.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Get the pattern of counts (e.g., [4,1] for four of a kind)
        List<Integer> counts = sorted.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        // ========== Check hands from best to worst ==========

        // Royal Flush
        if (flush && straight && cards.get(0).getRank().getValue() == 14) {
            return new HandResult(HandRank.ROYAL_FLUSH, tiebreakers);
        }

        // Straight Flush
        if (flush && straight) {
            return new HandResult(HandRank.STRAIGHT_FLUSH, List.of(getStraightHighCard(cards)));
        }

        // Four of a Kind
        if (counts.equals(List.of(4, 1))) {
            return new HandResult(HandRank.FOUR_OF_A_KIND, tiebreakers);
        }

        // Full House
        if (counts.equals(List.of(3, 2))) {
            return new HandResult(HandRank.FULL_HOUSE, tiebreakers);
        }

        // Flush
        if (flush) {
            return new HandResult(HandRank.FLUSH, tiebreakers);
        }

        // Straight
        if (straight) {
            return new HandResult(HandRank.STRAIGHT, List.of(getStraightHighCard(cards)));
        }

        // Three of a Kind
        if (counts.equals(List.of(3, 1, 1))) {
            return new HandResult(HandRank.THREE_OF_A_KIND, tiebreakers);
        }

        // Two Pair
        if (counts.equals(List.of(2, 2, 1))) {
            return new HandResult(HandRank.TWO_PAIR, tiebreakers);
        }

        // One Pair
        if (counts.equals(List.of(2, 1, 1, 1))) {
            return new HandResult(HandRank.ONE_PAIR, tiebreakers);
        }

        // High Card
        return new HandResult(HandRank.HIGH_CARD, tiebreakers);
    }

    /**
     * Check if all 5 cards are the same suit
     */
    private static boolean isFlush(List<Card> cards) {
        Suit first = cards.get(0).getSuit();
        return cards.stream().allMatch(c -> c.getSuit() == first);
    }

    /**
     * Check if 5 cards form a straight (consecutive ranks)
     */
    private static boolean isStraight(List<Card> cards) {
        // Normal straight check
        boolean normal = true;
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank().getValue() - cards.get(i + 1).getRank().getValue() != 1) {
                normal = false;
                break;
            }
        }
        if (normal) return true;

        // Special case: A-2-3-4-5 (wheel/low straight)
        List<Integer> values = cards.stream()
                .map(c -> c.getRank().getValue())
                .collect(Collectors.toList());
        return values.equals(List.of(14, 5, 4, 3, 2));
    }

    /**
     * Get the highest card in a straight
     * Special case: A-2-3-4-5 → high card is 5, not Ace
     */
    private static int getStraightHighCard(List<Card> cards) {
        List<Integer> values = cards.stream()
                .map(c -> c.getRank().getValue())
                .collect(Collectors.toList());

        // Wheel: A-2-3-4-5
        if (values.equals(List.of(14, 5, 4, 3, 2))) {
            return 5;
        }
        return values.get(0); // highest card
    }

    /**
     * Generate all combinations of size k from a list
     */
    private static List<List<Card>> getCombinations(List<Card> cards, int k) {
        List<List<Card>> result = new ArrayList<>();
        combine(cards, k, 0, new ArrayList<>(), result);
        return result;
    }

    private static void combine(List<Card> cards, int k, int start,
                                List<Card> current, List<List<Card>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < cards.size(); i++) {
            current.add(cards.get(i));
            combine(cards, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}