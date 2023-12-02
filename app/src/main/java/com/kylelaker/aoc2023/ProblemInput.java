package com.kylelaker.aoc2023;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.Buffer;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.springframework.beans.factory.parsing.Problem;

public class ProblemInput {
  private final String input;

  private ProblemInput(Stream<String> lines) {
    this.input = String.join("\n", lines.toList());
  }

  public String asString() {
    return this.input;
  }

  public List<String> asLines() {
    return Arrays.asList(this.input.split("\n"));
  }

  public <T> List<T> asTransformedLines(Function<String, T> function) {
    return this.asLines().stream().map(function).toList();
  }

  public IntStream asLinesOfNumbers() {
    return this.asLines().stream().mapToInt(Integer::parseInt);
  }

  public Reader asReader() {
    return new StringReader(this.input);
  }

  public static ProblemInput fromSystemIn() {
    return ProblemInput.fromInputStream(System.in);
  }

  public static ProblemInput fromInputStream(InputStream stream) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
      return new ProblemInput(reader.lines());
    } catch (IOException e) {
      return null;
    }
  }
}
