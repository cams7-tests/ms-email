package br.cams7.tests.ms.infra.logging;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.boot.logging.LogLevel.INFO;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;
import org.springframework.boot.logging.LogLevel;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogEntryExit {
  LogLevel value() default INFO;

  ChronoUnit unit() default MILLIS;

  boolean showArgs() default false;

  boolean showResult() default false;

  boolean showExecutionTime() default true;
}
