package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.domain.EmailEntity;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface GetEmailGateway {

  Mono<EmailEntity> findById(String emailId);
}
