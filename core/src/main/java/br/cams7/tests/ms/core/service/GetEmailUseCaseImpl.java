package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.exception.CommonExceptions.responseNotFoundException;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.out.GetEmailGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetEmailUseCaseImpl implements GetEmailUseCase {

  private static final String ERROR_MESSAGE = "Email not found";

  private final GetEmailGateway getEmailGateway;

  @Override
  public Mono<EmailEntity> execute(UUID emailId) {
    return getEmailGateway
        .findById(emailId)
        .switchIfEmpty(responseNotFoundException(ERROR_MESSAGE));
  }
}
