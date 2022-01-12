package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface SendEmailToQueueGateway {

  Mono<EmailStatusEnum> sendEmail(EmailEntity email);
}
