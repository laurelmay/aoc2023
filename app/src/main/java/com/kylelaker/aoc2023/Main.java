package com.kylelaker.aoc2023;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    public static void main(String... args) throws ReflectiveOperationException, IOException {

        if (args.length < 1 || args.length > 2) {
            System.err.println("Invalid arguments");
            return;
        }
        int day = Integer.parseInt(args[0]);
        ProblemInput input;
        if (args.length == 2) {
            input = ProblemInput.fromInputStream(new FileInputStream(new File(args[1])));
        } else {
            input = ProblemInput.fromSystemIn();
        }
        if (input == null) {
            System.err.println("Unable to read input");
            return;
        }

        SolutionFinder.findDay(day).forEach(solver -> SolutionInvoker.invoke(solver, input));
    }
}
