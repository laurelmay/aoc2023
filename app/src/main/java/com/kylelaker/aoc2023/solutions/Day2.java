package com.kylelaker.aoc2023.solutions;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.kylelaker.aoc2023.ProblemInput;
import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

final record CubeCount(int count, CubeColor color) {
}

final record CubeSet(Map<CubeColor, Integer> counts) {
}

final record Game(int id, Collection<CubeSet> sets) {
}

enum CubeColor {
    RED,
    GREEN,
    BLUE
}

@Day(number = 2)
public class Day2 {
    private static final int GAME_PREFIX = "GAME ".length();
    public static final Function<String, Game> GAME_PARSER = ((line) -> {
        String[] p1 = line.split(":");
        int gameId = Integer.valueOf(p1[0].substring(GAME_PREFIX));
        List<String> sets = Arrays.asList(p1[1].split(";"));
        List<CubeSet> gameSets = sets.stream().map(s -> s.trim()).map(s -> {
            Map<CubeColor, Integer> countMap = new HashMap<>();
            Arrays.asList(s.split(","))
                    .stream()
                    .map(count -> count.trim())
                    .map(count -> {
                        String[] parts = count.split(" ");
                        return new CubeCount(Integer.valueOf(parts[0]), CubeColor.valueOf(parts[1].toUpperCase()));
                    })
                    .forEach(count -> countMap.put(count.color(), count.count()));
            return new CubeSet(countMap);
        }).toList();
        return new Game(gameId, gameSets);

    });

    @Part(number = 1)
    public String part1(ProblemInput input) {
        List<Game> games = input.asTransformedLines(GAME_PARSER);
        int sumPossible = games.stream().filter((game) -> game.sets().stream()
                .allMatch(set -> (set.counts().getOrDefault(CubeColor.RED, 0) <= 12
                        && set.counts().getOrDefault(CubeColor.GREEN, 0) <= 13
                        && set.counts().getOrDefault(CubeColor.BLUE, 0) <= 14)))
                .mapToInt(Game::id)
                .reduce(0, (acc, id) -> acc + id);
        return String.valueOf(sumPossible);
    }

    @Part(number = 2)
    public String part2(ProblemInput input) {
        List<Game> games = input.asTransformedLines(GAME_PARSER);
        int powerSum = games.stream()
                .mapToInt(game -> {
                    Map<CubeColor, Integer> minRequired = new HashMap<>();
                    game.sets().stream()
                        .flatMap(set -> set.counts().entrySet().stream())
                        .forEach(entry -> minRequired.merge(entry.getKey(), entry.getValue(), (old, newV) -> {
                            if (old == null && newV == null) return 0;
                            if (old == null) return newV;
                            if (newV == null) return old;
                            return Math.max(old, newV);
                        }));
                    return minRequired.get(CubeColor.RED) * minRequired.get(CubeColor.GREEN) * minRequired.get(CubeColor.BLUE);
                })
                .reduce(0, (acc, id) -> acc + id);
        return String.valueOf(powerSum);
    }
}
