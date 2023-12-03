package com.kylelaker.aoc2023;

import com.kylelaker.aoc2023.annotations.Part;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class SolutionInvoker {
    public static void invoke(Class<?> solver, ProblemInput input) {
        Optional<Method> part1 = SolutionFinder.findPart(solver, 1);
        Optional<Method> part2 = SolutionFinder.findPart(solver, 2);
        List<Method> parts = Stream.concat(part1.stream(), part2.stream()).toList();

        try {
            Object instance = solver.getDeclaredConstructor().newInstance();
            for (Method partSolver : parts) {
                Part part = partSolver.getAnnotation(Part.class);
                Class<?>[] parameterTypes = partSolver.getParameterTypes();
                if (parameterTypes.length != 1) {
                    throw new ReflectiveOperationException("The method does not accept the correct number of parameters");
                }
                Class<?> inputType = parameterTypes[0];
                Object returnValue;
                if (inputType.equals(String.class)) {
                    returnValue = partSolver.invoke(instance, input.asString());
                } else if (inputType.equals(List.class)) {
                    returnValue = partSolver.invoke(instance, input.asLines());
                } else if (inputType.equals(ProblemInput.class)) {
                    returnValue = partSolver.invoke(instance, input);
                } else {
                    throw new IllegalArgumentException("Unsupported input parameter type: " + inputType);
                }
                System.out.println("Part " + part.value() + ": " + returnValue);
            }
        } catch (ReflectiveOperationException e) {
            System.err.println("Unable to invoke the solution for " + solver.getName());
            e.printStackTrace();
            return;
        }
    }
}
