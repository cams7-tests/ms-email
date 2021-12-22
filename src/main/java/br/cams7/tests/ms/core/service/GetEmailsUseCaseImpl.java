package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetEmailsUseCaseImpl implements GetEmailsUseCase {

  private final GetEmailsRepository getEmailsRepository;

  @Override
  public List<EmailEntity> findAll(PageDTO page) {
    // inserir manipulação de dados/regras
    return getEmailsRepository.findAll(page);
  }
}
