package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface GetEmailUseCase {

  Mono<EmailResponseDTO> findById(UUID emailId);
}
