package br.cams7.tests.ms.core.port.in.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonExceptions {
  public static final int NOT_FOUND = 404;
  public static final int INTERNAL_SERVER_ERROR = 500;

  public static <T> Mono<T> responseNotFoundException() {
    return Mono.error(new ResponseStatusException(NOT_FOUND));
  }

  public static <T> Mono<T> responseInternalServerErrorException() {
    return Mono.error(new ResponseStatusException(INTERNAL_SERVER_ERROR));
  }
}
