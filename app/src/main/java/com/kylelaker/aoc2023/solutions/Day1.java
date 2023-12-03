package com.kylelaker.aoc2023.solutions;

import com.kylelaker.aoc2023.ProblemInput;
import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

import java.util.List;
import java.util.Map;

@Day(1)
public class Day1 {
    private record IntLoc(Map.Entry<String, Integer> item, int location) {
    }

    private final Map<String, Integer> values = Map.of(
            "one", 1,
            "two", 2,
            "three", 3,
            "four", 4,
            "five", 5,
            "six", 6,
            "seven", 7,
            "eight", 8,
            "nine", 9
    );

    @Part(1)
    public int part1(ProblemInput input) {
        return input.asTransformedLines((line) -> {
            List<String> digits = line.chars().filter(c -> Character.isDigit(c)).<String>mapToObj(c -> Character.toString(c)).toList();
            return Integer.parseInt("" + digits.get(0) + digits.get(digits.size() - 1));
        }).stream().reduce(0, Integer::sum);
    }

    @Part(2)
    public int part2(ProblemInput input) {
        return input.asLines()
                .stream()
                .map((line) -> Integer.parseInt("" + findFirstNumber(line) + findLastNumber(line)))
                .reduce(0, Integer::sum);
    }

    private int findFirstNumber(String line) {
        return this.values.entrySet().stream()
                .<IntLoc>mapMulti((entry, consumer) -> {
                    consumer.accept(new IntLoc(entry, line.indexOf(entry.getKey())));
                    consumer.accept(new IntLoc(entry, line.indexOf("" + entry.getValue())));
                })
                .filter(loc -> loc.location() >= 0)
                .min((a, b) -> a.location() - b.location())
                .get()
                .item()
                .getValue();
    }

    private int findLastNumber(String line) {
        return this.values.entrySet().stream()
                .<IntLoc>mapMulti((entry, consumer) -> {
                    consumer.accept(new IntLoc(entry, line.lastIndexOf(entry.getKey())));
                    consumer.accept(new IntLoc(entry, line.lastIndexOf("" + entry.getValue())));
                })
                .filter(loc -> loc.location() >= 0)
                .max((a, b) -> a.location() - b.location())
                .get()
                .item()
                .getValue();
    }
}
