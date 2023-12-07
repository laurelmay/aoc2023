package com.kylelaker.aoc2023.solutions;

import java.util.Arrays;
import java.util.SequencedCollection;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import com.kylelaker.aoc2023.ProblemInput;
import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

@Day(6)
public class Day6 {

  public static record RaceRecord(long time, long distance) {
    public boolean beats(RaceRecord other) {
      if (this.time != other.time) {
        throw new IllegalArgumentException("The records have different times and cannot be compared");
      }
      return this.distance > other.distance;
    }

    public boolean beatenBy(RaceRecord other) {
      if (this.time != other.time) {
        throw new IllegalArgumentException("The records have different times and cannot be compared");
      }
      return this.distance < other.distance;
    }
  }

  public static record Boat(long startingSpeed, long accelerationRate) {
    public SequencedCollection<RaceRecord> optionsFor(long totalTime) {
      return LongStream.rangeClosed(0, totalTime)
        .parallel()
        .map(buttonTime -> (startingSpeed + (buttonTime * accelerationRate)) * (totalTime - buttonTime))
        .mapToObj(distance -> new RaceRecord(totalTime, distance))
        .toList();
    }
  }

  private static SequencedCollection<RaceRecord> parseRecords(SequencedCollection<String> lines) {
    String[] times = lines.getFirst().split("\\s+");
    String[] distances = lines.getLast().split("\\s+");

    return IntStream.range(1, times.length)
      .mapToObj(idx -> new RaceRecord(Long.parseLong(times[idx]), Long.parseLong(distances[idx])))
      .toList();
  }

  private static RaceRecord parseAsOneRecord(SequencedCollection<String> lines) {
    String[] times = lines.getFirst().split("\\s+");
    String[] distances = lines.getLast().split("\\s+");

    long time = Long.parseLong(Arrays.stream(times).skip(1).reduce("", String::concat));
    long distance = Long.parseLong(Arrays.stream(distances).skip(1).reduce("", String::concat));
    return new RaceRecord(time, distance);
  }

  private static long winningButtonTimes(RaceRecord record) {
    // distance = (buttonTime) * (totalTime - buttonTime)
    // D = B * (T - B)
    // D = BT - B^2
    // B^2 - BT + D = 0
    long t = record.time();
    long d = record.distance();

    double button1 = (t - Math.sqrt(t * t - 4 * d)) / 2;
    double button2 = (t + Math.sqrt(t * t - 4 * d)) / 2;

    // If the roots are integers, then we can _tie_ the record with these times but we really
    // need to beat it. So there's one less option to win.
    long offset = button1 % 1 == 0 && button2 % 1 == 0 ? -1 : 0;

    return (long) Math.floor(button2) - (long)Math.floor(button1) + offset;
  }

  @Part(1)
  public long part1(ProblemInput input) {
    SequencedCollection<RaceRecord> records = parseRecords(input.asLines());
    Boat boat = new Boat(0, 1);

    return records.parallelStream()
      .mapToLong(record -> boat.optionsFor(record.time()).parallelStream().filter(record::beatenBy).count())
      .reduce(1, (a, b) -> a * b);
  }

  @Part(1)
  public long part1WithMath(ProblemInput input) {
    SequencedCollection<RaceRecord> records = parseRecords(input.asLines());
    return records.stream().mapToLong(Day6::winningButtonTimes).reduce(1, (a, b) -> a * b);
  }

  @Part(2)
  public long part2(ProblemInput input) {
    RaceRecord record = parseAsOneRecord(input.asLines());
    Boat boat = new Boat(0, 1);

    return boat.optionsFor(record.time())
      .parallelStream()
      .filter(record::beatenBy)
      .count();
  }

  @Part(2)
  public long part2WithMath(ProblemInput input) {
    RaceRecord record = parseAsOneRecord(input.asLines());
    return winningButtonTimes(record);
  }

}
