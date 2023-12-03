package com.kylelaker.aoc2023.solutions;

import com.kylelaker.aoc2023.ProblemInput;
import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

import java.util.*;
import java.util.function.Function;


@Day(2)
public class Day2 {
    public final record CubeCount(int count, CubeColor color) {}
    public final record CubeSet(Map<CubeColor, Integer> counts) {}
    public final record Game(int id, Collection<CubeSet> sets) {}
    public enum CubeColor { RED, GREEN, BLUE }

    private static final int GAME_PREFIX = "GAME ".length();
    static final Function<String, Game> GAME_PARSER = ((line) -> {
        String[] p1 = line.split(":");
        int gameId = Integer.parseInt(p1[0].substring(GAME_PREFIX));
        List<String> sets = Arrays.asList(p1[1].split(";"));
        List<CubeSet> gameSets = sets.stream().map(String::trim).map(s -> {
            Map<CubeColor, Integer> countMap = new HashMap<>();
            Arrays.stream(s.split(","))
                    .map(String::trim)
                    .map(count -> {
                        String[] parts = count.split(" ");
                        return new CubeCount(Integer.parseInt(parts[0]), CubeColor.valueOf(parts[1].toUpperCase()));
                    })
                    .forEach(count -> countMap.put(count.color(), count.count()));
            return new CubeSet(countMap);
        }).toList();
        return new Game(gameId, gameSets);

    });

    @Part(1)
    public int part1(ProblemInput input) {
        SequencedCollection<Game> games = input.asTransformedLines(GAME_PARSER);
        return games.stream().filter((game) -> game.sets().stream()
                        .allMatch(set -> (set.counts().getOrDefault(CubeColor.RED, 0) <= 12
                                && set.counts().getOrDefault(CubeColor.GREEN, 0) <= 13
                                && set.counts().getOrDefault(CubeColor.BLUE, 0) <= 14)))
                .mapToInt(Game::id)
                .reduce(0, Integer::sum);
    }

    @Part(2)
    public int part2(ProblemInput input) {
        SequencedCollection<Game> games = input.asTransformedLines(GAME_PARSER);
        return games.stream()
                .mapToInt(game -> {
                    Map<CubeColor, Integer> minRequired = new HashMap<>();
                    game.sets().stream()
                            .flatMap(set -> set.counts().entrySet().stream())
                            .forEach(entry -> minRequired.merge(entry.getKey(), entry.getValue(), Math::max));
                    return minRequired.get(CubeColor.RED) * minRequired.get(CubeColor.GREEN) * minRequired.get(CubeColor.BLUE);
                })
                .reduce(0, Integer::sum);
    }
}
