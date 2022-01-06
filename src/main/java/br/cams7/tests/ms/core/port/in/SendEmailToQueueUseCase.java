package br.cams7.tests.ms.core.port.in;

import reactor.core.publisher.Mono;

public interface SendEmailToQueueUseCase {

  Mono<Void> sendEmail(EmailVO email);
}
