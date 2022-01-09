package br.cams7.tests.ms.infra.logging;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.boot.logging.LogLevel.INFO;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Objects;
import java.util.StringJoiner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class LoggingAspect {

  private static final String LOGGING_PATH = "br.cams7.tests.ms.infra.logging";
  private static final String USECASE_PATH = "br.cams7.tests.ms.core.service";

  @Value("${logEntryExit.showLog}")
  private boolean showLog;

  @Value("${logEntryExit.showArgs}")
  private boolean showArgs;

  @Value("${logEntryExit.showResult}")
  private boolean showResult;

  @Value("${logEntryExit.showExecutionTime}")
  private boolean showExecutionTime;

  @Value("${logEntryExit.level}")
  private LogLevel level;

  @Value("${logEntryExit.unit}")
  private ChronoUnit unit;

  @Around("@annotation(" + LOGGING_PATH + ".LogEntryExit)")
  public Object logMethodByAnnotation(ProceedingJoinPoint point) throws Throwable {
    var methodSignature = (MethodSignature) point.getSignature();
    Method method = methodSignature.getMethod();
    var annotation = method.getAnnotation(LogEntryExit.class);

    showArgs = annotation.showArgs();
    showResult = annotation.showResult();
    showExecutionTime = annotation.showExecutionTime();
    level = annotation.value();
    unit = annotation.unit();

    return log(point);
  }

  @Around("execution(* " + USECASE_PATH + "..*(..)))")
  public Object logMethod(ProceedingJoinPoint point) throws Throwable {
    return log(point);
  }

  private Object log(ProceedingJoinPoint point) throws Throwable {
    if (!showLog) return point.proceed();
    var codeSignature = (CodeSignature) point.getSignature();
    var methodSignature = (MethodSignature) point.getSignature();
    Method method = methodSignature.getMethod();
    Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());
    String methodName = method.getName();
    Object[] methodArgs = point.getArgs();
    String[] methodParams = codeSignature.getParameterNames();

    if (level == null) level = INFO;

    if (unit == null) unit = MILLIS;

    log(logger, entry(methodName, methodParams, methodArgs));
    return getResponse(point, logger, methodName);
  }

  private void log(Logger logger, String message) {
    switch (level) {
      case DEBUG:
        logger.debug(message);
        break;
      case TRACE:
        logger.trace(message);
        break;
      case WARN:
        logger.warn(message);
        break;
      case ERROR:
      case FATAL:
        logger.error(message);
        break;
      default:
        logger.info(message);
        break;
    }
  }

  private String entry(String methodName, String[] params, Object[] args) {
    var message = new StringJoiner(" ").add("Started").add(methodName).add("method");
    if (showArgs
        && Objects.nonNull(params)
        && Objects.nonNull(args)
        && params.length == args.length) {
      var values = new HashMap<>(params.length);
      for (int i = 0; i < params.length; i++) {
        values.put(params[i], args[i]);
      }
      message.add("with args:").add(values.toString());
    }
    return message.toString();
  }

  private Object getResponse(ProceedingJoinPoint point, Logger logger, String methodName)
      throws Throwable {
    var start = Instant.now();
    var response = point.proceed();
    if (response != null) {
      if (response instanceof Mono || response instanceof Flux) {
        if (response instanceof Mono)
          response =
              ((Mono<?>) response).doOnNext(data -> exitLog(logger, methodName, start, data));
        else
          response =
              ((Flux<?>) response).doOnNext(data -> exitLog(logger, methodName, start, data));
      } else exitLog(logger, methodName, start, response);
    }
    return response;
  }

  private void exitLog(Logger logger, String methodName, Instant start, Object response) {
    log(logger, exit(methodName, start, response));
  }

  private String exit(String methodName, Instant start, Object response) {
    var end = Instant.now();
    var duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());
    var message = new StringJoiner(" ").add("Finished").add(methodName).add("method");
    if (showExecutionTime) {
      message.add("in").add(duration);
    }
    if (showResult) {
      message.add("with return:").add(response.toString());
    }
    return message.toString();
  }
}
