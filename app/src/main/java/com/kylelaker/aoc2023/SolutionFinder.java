package com.kylelaker.aoc2023;

import com.kylelaker.aoc2023.annotations.Day;
import com.kylelaker.aoc2023.annotations.Part;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class SolutionFinder {
    public static Collection<Class<?>> findDay(int number) {
        var scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(Day.class));
        return scanner.findCandidateComponents(SolutionFinder.class.getPackageName())
                .stream()
                .map(BeanDefinition::getBeanClassName)
                .<Class<?>>mapMulti((name, consumer) -> {
                    try {
                        consumer.accept(Class.forName(name));
                    } catch (ClassNotFoundException e) {
                        // Do nothing (don't accept the value)
                    }
                })
                .filter(clazz -> clazz.getAnnotation(Day.class).value() == number)
                .toList();
    }

    public static Collection<Method> findParts(Class<?> solver, int number) {
        return Arrays.stream(solver.getDeclaredMethods()).filter(method -> {
            Part annot = method.getAnnotation(Part.class);
            return annot != null && annot.value() == number;
        }).toList();
    }
}
