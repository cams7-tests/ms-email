package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.domain.EmailEntity;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GetEmailUseCase {

  Mono<EmailEntity> execute(String emailId);
}
