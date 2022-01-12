package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface SendEmailToQueueUseCase {

  Mono<EmailStatusEnum> execute(EmailVO email);
}
