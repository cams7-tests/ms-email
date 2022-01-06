package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.domain.EmailEntity;
import reactor.core.publisher.Mono;

public interface SendEmailDirectlyUseCase {

  Mono<EmailEntity> sendEmail(EmailVO email);
}
