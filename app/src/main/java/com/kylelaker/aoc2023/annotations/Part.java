package com.kylelaker.aoc2023.annotations;

import com.kylelaker.aoc2023.ProblemInput;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Part {
    int number();

    Class<?> inputType() default ProblemInput.class;
}
