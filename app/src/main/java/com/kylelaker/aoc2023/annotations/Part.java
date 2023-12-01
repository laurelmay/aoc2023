package com.kylelaker.aoc2023.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.kylelaker.aoc2023.ProblemInput;

@Retention(RetentionPolicy.RUNTIME)
public @interface Part {
  int number();

  Class<?> inputType() default ProblemInput.class;
}
