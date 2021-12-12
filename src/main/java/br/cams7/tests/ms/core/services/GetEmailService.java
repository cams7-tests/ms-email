package br.cams7.tests.ms.core.services;


import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.ports.in.GetEmailUseCase;
import br.cams7.tests.ms.core.ports.out.EmailRepositoryPort;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetEmailService implements GetEmailUseCase {

  private final EmailRepositoryPort emailRepositoryPort;

  @Override
  public Optional<EmailEntity> findById(UUID emailId) {
    // inserir manipulação de dados/regras
    return emailRepositoryPort.findById(emailId);
  }
}
