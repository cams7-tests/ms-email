package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.domain.EmailEntity;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetEmailGateway {

  Mono<EmailEntity> findById(UUID emailId);
}
