package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetEmailUseCaseImpl implements GetEmailUseCase {

  private final GetEmailRepository getEmailRepository;

  @Override
  public Optional<EmailEntity> findById(UUID emailId) {
    // inserir manipulação de dados/regras
    return getEmailRepository.findById(emailId);
  }
}
