package com.kylelaker.aoc2023;

public class Main {
  public static void main(String... args) throws ReflectiveOperationException {

    if (args.length != 1) {
      System.err.println("Invalid arguments");
      return;
    }
    int day = Integer.parseInt(args[0]);

    ProblemInput input = ProblemInput.fromSystemIn();
    if (input == null) {
      System.err.println("Unable to read input");
      return;
    }

    SolutionFinder.findDay(day)
      .ifPresentOrElse(
        solver -> SolutionInvoker.invoke(solver, input),
        () -> System.err.println("No solution for Day " + day)
      );
  }
}
