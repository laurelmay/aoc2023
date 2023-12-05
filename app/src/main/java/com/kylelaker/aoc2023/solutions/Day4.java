package com.kylelaker.aoc2023.solutions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.SequencedCollection;
import java.util.Set;
import java.util.stream.Collectors;

import com.kylelaker.aoc2023.ProblemInput;
import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

@Day(4)
public class Day4 {

    public record ScratchCard(int number, Set<Integer> winningNumbers, Set<Integer> actualNumbers) {
        public int score() {
            int matches = matches();
            if (matches == 0) {
                return 0;
            }
            return (int) Math.pow(2, matches - 1);
        }
        public int matches() {
            Set<Integer> losingNumbers = new HashSet<>(actualNumbers);
            losingNumbers.removeAll(winningNumbers);

            return actualNumbers.size() - losingNumbers.size();
        }
    }

    private static final String PREFIX = "Card ";
    private static final String SEPARATOR = "\\|";

    private static Set<Integer> numbersToSet(String[] numbers) {
        return Arrays.stream(numbers)
            .parallel()
            .map(String::trim)
            .map(Integer::valueOf)
            .collect(Collectors.toUnmodifiableSet());
    }

    private static ScratchCard parseLine(String line) {
        String[] idAndNumbers = line.split(":");
        int id = Integer.parseInt(idAndNumbers[0].substring(PREFIX.length()).trim());
        String[] winningAndOwnNumbers = idAndNumbers[1].trim().split(SEPARATOR);
        String[] winning = winningAndOwnNumbers[0].trim().split("\\s+");
        String[] actual = winningAndOwnNumbers[1].trim().split("\\s+");
        return new ScratchCard(id, numbersToSet(winning), numbersToSet(actual));
    }

    @Part(1)
    public int part1(ProblemInput input) {
        return input.asLines().parallelStream()
            .map(Day4::parseLine)
            .mapToInt(ScratchCard::score)
            .sum();
    }

    @Part(2)
    public int part2(ProblemInput input) {
        SequencedCollection<ScratchCard> originalCards = input.asTransformedLines(Day4::parseLine);
        // A HashMap allows a clearer expression of the 1-based indexing for the card numbers
        Map<Integer, Integer> cardCounts = HashMap.newHashMap(originalCards.size());
        originalCards.forEach(card -> cardCounts.put(card.number(), 1));
        originalCards.forEach(card -> {
            int count = cardCounts.get(card.number());
            for (int i = 1; i <= card.matches(); i++) {
                cardCounts.computeIfPresent(card.number() + i, (k, v) -> v + count);
            }
        });
        return cardCounts.values().parallelStream().reduce(0, Integer::sum);
    }
}
