package com.kylelaker.aoc2023.solutions;

import com.kylelaker.aoc2023.ProblemInput;
import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

import java.util.*;
import java.util.stream.IntStream;

@Day(3)
public class Day3 {

    public record Index(int row, int col) {}

    public record Range(Index start, Index end) {
        public Range(Index start, Index end) {
            if (end.col < start.col) {
                throw new IllegalArgumentException();
            }
            if (end.row < start.row) {
                throw new IllegalArgumentException();
            }
            this.start = start;
            this.end = end;
        }

        public boolean contains(Index idx) {
            return idx != null
                    && this.start.row <= idx.row
                    && this.end.row >= idx.row
                    && this.start.col <= idx.col
                    && this.end.col >= idx.col;
        }

        public boolean adjacentTo(Index idx) {
            if (idx == null) {
                return false;
            }
            return new Range(new Index(this.start.row - 1, this.start.col - 1),
                    new Index(this.end.row + 1, this.end.col + 1)).contains(idx);
        }
    }

    public record NumberAtLocation(int value, Range location) {}

    public record SymbolLocation(char value, Index location) {}

    public record SchematicData(SequencedCollection<NumberAtLocation> numbers, SequencedCollection<SymbolLocation> symbols) {}

    private SchematicData parseSchematic(char[][] schematic) {
        List<NumberAtLocation> numbers = new ArrayList<>();
        List<SymbolLocation> symbols = new ArrayList<>();
        for (int row = 0; row < schematic.length; row++) {
            int numberBuffer = 0;
            Index numberStart = null;
            Index numberEnd = null;
            for (int col = 0; col < schematic.length; col++) {
                char character = schematic[row][col];
                if (Character.isDigit(character)) {
                    numberBuffer *= 10;
                    numberBuffer += Character.getNumericValue(character);
                    numberEnd = new Index(row, col);
                    if (numberStart == null) {
                        numberStart = numberEnd;
                    }
                }
                if ((!Character.isDigit(character) || col == schematic.length - 1) && numberBuffer != 0) {
                    numbers.add(new NumberAtLocation(numberBuffer, new Range(numberStart, numberEnd)));
                    numberBuffer = 0;
                    numberStart = null;
                    numberEnd = null;
                }
                if (!Character.isDigit(character) && character != '.') {
                    symbols.add(new SymbolLocation(character, new Index(row, col)));
                }
            }
        }

        return new SchematicData(numbers, symbols);
    }

    @Part(1)
    public int part1(ProblemInput input) {
        SchematicData schematic = parseSchematic(input.asCharGrid());
        SequencedCollection<NumberAtLocation> numbers = schematic.numbers();
        SequencedCollection<SymbolLocation> symbols = schematic.symbols();
        SequencedCollection<NumberAtLocation> partNumbers = numbers.stream()
                .filter(num -> symbols.stream().anyMatch(symbol -> num.location().adjacentTo(symbol.location())))
                .toList();
        return partNumbers.stream().mapToInt(NumberAtLocation::value).reduce(0, Integer::sum);
    }

    @Part(2)
    public int part2(ProblemInput input) {
        SchematicData schematic = parseSchematic(input.asCharGrid());
        SequencedCollection<NumberAtLocation> numbers = schematic.numbers();
        SequencedCollection<SymbolLocation> symbols = schematic.symbols();

        IntStream gearRatios = symbols
            .stream()
            .filter(s -> s.value() == '*')
            .mapMultiToInt((symbol, consumer) -> {
                SequencedCollection<NumberAtLocation> adjacentNumbers = numbers.stream()
                    .filter((number) -> number.location().adjacentTo(symbol.location()))
                    .toList();
                if (adjacentNumbers.size() != 2) {
                    return;
                }
                consumer.accept(adjacentNumbers.stream().mapToInt(NumberAtLocation::value).reduce(1, (a, b) -> a * b));
             });
        return gearRatios.reduce(0, Integer::sum);
    }
}
