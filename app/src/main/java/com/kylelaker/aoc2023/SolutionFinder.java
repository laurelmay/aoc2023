package com.kylelaker.aoc2023;

import java.lang.reflect.Method;
import java.util.Optional;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;

public class SolutionFinder {
  public static Optional<Class<?>> findDay(int number) {
    var scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Day.class));
    return scanner.findCandidateComponents("com.kylelaker.aoc2023.solutions")
      .stream()
      .map(bean -> bean.getBeanClassName())
      .<Class<?>>mapMulti((name, consumer) -> {
        try {
          consumer.accept(Class.forName(name));
        } catch (ClassNotFoundException e) {
          // Do nothing (don't accept the value)
        }
      })
      .filter(clazz -> clazz.getAnnotation(Day.class).number() == number)
      .findFirst();
  }

  public static Optional<Method> findPart(Class<?> solver, int number) {
    Method[] methods = solver.getDeclaredMethods();
    for (Method method : methods) {
      Part annot = method.getAnnotation(Part.class);
      if (annot != null && annot.number() == number) {
        return Optional.of(method);
      }
    }
    return Optional.empty();
  }
}
