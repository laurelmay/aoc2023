package com.kylelaker.aoc2023;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import com.kylelaker.aoc2023.annotations.Part;

public class SolutionInvoker {
  public static void invoke(Class<?> solver, ProblemInput input) {
    Optional<Method> part1 = SolutionFinder.findPart(solver, 1);
    Optional<Method> part2 = SolutionFinder.findPart(solver, 2);
    List<Method> parts = Stream.concat(part1.stream(), part2.stream()).toList();

    try {
      Object instance = solver.getDeclaredConstructor().newInstance();
      for (Method part : parts) {
        Part data = part.getAnnotation(Part.class);
        Object returnValue = part.invoke(instance, input);
        System.out.println("Part " + data.number() + ": " + returnValue);
      }
    } catch (ReflectiveOperationException e) {
      System.err.println("Unable to invoke the solution for " + solver.getName());
      e.printStackTrace();
      return;
    }
  }
}
