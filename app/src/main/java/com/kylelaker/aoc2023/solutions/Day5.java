package com.kylelaker.aoc2023.solutions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.SequencedCollection;
import java.util.stream.LongStream;

import com.kylelaker.aoc2023.ProblemInput;
import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

@Day(5)
public class Day5 {
    public enum AlmanacDataType { SEED, SOIL, FERTILIZER, WATER, LIGHT, TEMPERATURE, HUMIDITY, LOCATION }
    public record OffsetRange(AlmanacDataType source, AlmanacDataType dest, long srcStart, long destStart, long range) { }
    public record SeedRange(long start, long length) { }

    private long lookup(SequencedCollection<OffsetRange> ranges, AlmanacDataType source, AlmanacDataType dest, long number) {
        return ranges.stream().parallel()
        // ranges are [start, start + length). Wasted a lot of time debugging the fact that I used >= here originally.
        .filter(r -> r.source() == source && r.dest() == dest && r.srcStart() <= number && r.srcStart() + r.range() > number)
        .findFirst()
        .map(it -> it.destStart() + (number - it.srcStart()))
        .orElse(number);
    }

    private long fullLookup(SequencedCollection<OffsetRange> ranges, LongStream seeds) {
        return seeds
            // call parallel() on the stream; it may already be parallel which is fine and
            // since this method ends with a terminal operation, it's pretty safe to go ahead and
            // roll with this.
            .parallel()
            .map(seed -> lookup(ranges, AlmanacDataType.SEED, AlmanacDataType.SOIL, seed))
            .map(soil -> lookup(ranges, AlmanacDataType.SOIL, AlmanacDataType.FERTILIZER, soil))
            .map(fertilizer -> lookup(ranges, AlmanacDataType.FERTILIZER, AlmanacDataType.WATER, fertilizer))
            .map(water -> lookup(ranges, AlmanacDataType.WATER, AlmanacDataType.LIGHT, water))
            .map(light -> lookup(ranges, AlmanacDataType.LIGHT, AlmanacDataType.TEMPERATURE, light))
            .map(temp -> lookup(ranges, AlmanacDataType.TEMPERATURE, AlmanacDataType.HUMIDITY, temp))
            .map(humidity -> lookup(ranges, AlmanacDataType.HUMIDITY, AlmanacDataType.LOCATION, humidity))
            .min()
            .getAsLong();
    }

    private SequencedCollection<OffsetRange> parseRanges(SequencedCollection<String> lines) {
        List<OffsetRange> ranges = new ArrayList<>();
        AlmanacDataType from = null;
        AlmanacDataType to = null;
        for (String line : lines) {
            if (line.startsWith("seeds: ")) {
                continue;
            }
            if (line.endsWith(" map:")) {
                String[] section = line.split(" ")[0].split("\\-");
                from = AlmanacDataType.valueOf(section[0].toUpperCase());
                to = AlmanacDataType.valueOf(section[2].toUpperCase());
                continue;
            }
            if (line.isBlank()) {
                continue;
            }

            long[] mapEntries = ProblemInput.stringToLongs(line);
            ranges.add(new OffsetRange(from, to, mapEntries[1], mapEntries[0], mapEntries[2]));
        }
        return ranges;
    }

    private SequencedCollection<SeedRange> parseSeedRanges(long[] seedData) {
        List<SeedRange> ranges = new ArrayList<>();
        for (int i = 0; i <= seedData.length - 2; i += 2) {
            ranges.add(new SeedRange(seedData[i], seedData[i + 1]));
        }
        return ranges;
    }

    @Part(1)
    public long part1(ProblemInput input) {
        SequencedCollection<String> lines = input.asLines();
        long[] seeds = ProblemInput.stringToLongs(lines.getFirst().substring("seeds: ".length()));
        SequencedCollection<OffsetRange> ranges = parseRanges(lines);
        return fullLookup(ranges, Arrays.stream(seeds));
    }

    // There is almost certainly a more efficient solution here, based around tracking the
    // possible paths through the almanac math and then doing some reverse looking up. That'd
    // likely be many many times better than this... but this works sorta well enough.
    @Part(2)
    public long part2(ProblemInput input) {
        SequencedCollection<String> lines = input.asLines();
        long[] seeds = ProblemInput.stringToLongs(lines.getFirst().substring("seeds: ".length()));
        SequencedCollection<SeedRange> seedRanges = parseSeedRanges(seeds);
        SequencedCollection<OffsetRange> ranges = parseRanges(lines);
        // Use a bunch of `parallel` to make the computer cry but finish at least somewhat
        // reasonably quickly.
        return seedRanges.stream()
            .parallel()
            // Using `mapToLong` and performing a nested operation seems to perform better than
            // using a `flatMap` in this case. It seems to allow for more aggressive parallelization.
            .mapToLong(range -> fullLookup(ranges, LongStream.range(range.start(), range.start() + range.length()).parallel()))
            .min()
            .getAsLong();
    }

}
