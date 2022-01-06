package br.cams7.tests.ms.core.port.in;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface SendEmailToQueueUseCase {

  Mono<Void> execute(EmailVO email);
}
