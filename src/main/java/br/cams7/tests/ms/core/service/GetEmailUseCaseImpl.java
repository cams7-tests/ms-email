package br.cams7.tests.ms.core.service;

import static br.cams7.tests.ms.core.port.in.exception.CommonExceptions.responseNotFoundException;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetEmailUseCaseImpl implements GetEmailUseCase {

  private final GetEmailRepository getEmailRepository;

  @Override
  public Mono<EmailEntity> findById(UUID emailId) {
    return getEmailRepository.findById(emailId).switchIfEmpty(responseNotFoundException());
  }
}
