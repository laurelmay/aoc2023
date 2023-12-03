package com.kylelaker.aoc2023;

import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

public class SolutionFinder {
    public static Collection<Class<?>> findDay(int number) {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Day.class));
        return scanner.findCandidateComponents("com.kylelaker.aoc2023.solutions")
                .stream()
                .map(BeanDefinition::getBeanClassName)
                .<Class<?>>mapMulti((name, consumer) -> {
                    try {
                        consumer.accept(Class.forName(name));
                    } catch (ClassNotFoundException e) {
                        // Do nothing (don't accept the value)
                    }
                })
                .filter(clazz -> clazz.getAnnotation(Day.class).number() == number)
                .toList();
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
