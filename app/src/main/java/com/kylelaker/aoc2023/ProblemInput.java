package com.kylelaker.aoc2023;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.SequencedCollection;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utilities for handling Advent of Code inputs.
 */
public class ProblemInput {
    private final String input;

    private ProblemInput(Stream<String> lines) {
        this.input = String.join("\n", lines.toList());
    }

    /**
     * Get the input as a single string.
     */
    public String asString() {
        return this.input;
    }

    /**
     * Get the input as individual lines.
     * <p>
     * The list returned is unmodifiable.
     */
    public SequencedCollection<String> asLines() {
        return List.copyOf(Arrays.asList(this.input.split("\n")));
    }

    /**
     * Apply a transformation to lines of the input.
     *
     * @param function - the transformation to apply to each line
     */
    public <T> SequencedCollection<T> asTransformedLines(Function<String, T> function) {
        return this.asLines().stream().map(function).toList();
    }

    /**
     * Parse each line as an integer.
     */
    public IntStream asLinesOfNumbers() {
        return this.asLines().stream().mapToInt(Integer::parseInt);
    }

    /**
     * Get the input as a Reader.
     */
    public Reader asReader() {
        return new StringReader(this.input);
    }

    /**
     * Returns the input as a grid of 1-character Strings.
     */
    public String[][] asGrid() {
        List<String> lines = List.copyOf(this.asLines());
        int size = lines.size();
        String[][] grid = new String[size][size];

        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).split("");
        }
        return grid;
    }

    public char[][] asCharGrid() {
        List<String> lines = List.copyOf(this.asLines());
        int size = lines.size();
        char[][] grid = new char[size][size];

        for (int i = 0; i < lines.size(); i++) {
            grid[i] = lines.get(i).toCharArray();
        }
        return grid;
    }

    /**
     * Build the input utility from {@link System.in}.
     */
    public static ProblemInput fromSystemIn() {
        return ProblemInput.fromInputStream(System.in);
    }

    /**
     * Build the input utility from a generic input stream.
     * <p>
     * This method will close the given input stream.
     *
     * @param stream - the {@link InputStream} to read from
     */
    public static ProblemInput fromInputStream(InputStream stream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            return new ProblemInput(reader.lines());
        } catch (IOException e) {
            return null;
        }
    }

    private static long[] stringToLongs(String input, String separator) {
        return Arrays.stream(input.split(separator)).mapToLong(Long::parseLong).toArray();
    }

    private static int[] stringToInts(String input, String separator) {
        return Arrays.stream(input.split(separator)).mapToInt(Integer::parseInt).toArray();
    }

    public static int[] stringToInts(String input) {
        return stringToInts(input, "\\s+");
    }

    public static long[] stringToLongs(String input) {
        return stringToLongs(input, "\\s+");
    }
}
