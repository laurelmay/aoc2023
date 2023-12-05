/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package com.kylelaker.aoc2023.solutions;

import com.kylelaker.aoc2023.ProblemInput;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Day4Test {
    @Test
    void day4SolvesPart1() throws Exception {
        // GIVEN
        Day4 solver = new Day4();
        // WHEN
        File inputFile = new File(getClass().getClassLoader().getResource("inputs/4a.txt").getFile());
        ProblemInput input = ProblemInput.fromInputStream(new FileInputStream(inputFile));
        // THEN
        assertEquals(13, solver.part1(input));
    }

    @Test
    void day4SovesPart2SampleInput() throws Exception {
        // GIVEN
        Day4 solver = new Day4();
        // WHEN
        File inputFile = new File(getClass().getClassLoader().getResource("inputs/4a.txt").getFile());
        ProblemInput input = ProblemInput.fromInputStream(new FileInputStream(inputFile));
        // THEN
        assertEquals(30, solver.part2(input));
    }
}