package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import reactor.core.publisher.Mono;

public interface SendEmailDirectlyUseCase {

  Mono<EmailResponseDTO> sendEmail(EmailVO email);
}
