package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.domain.EmailEntity;
import reactor.core.publisher.Mono;

public interface SaveEmailRepository {
  Mono<EmailEntity> save(EmailEntity email);
}
