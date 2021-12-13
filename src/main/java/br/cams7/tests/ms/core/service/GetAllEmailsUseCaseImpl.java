package br.cams7.tests.ms.core.service;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.port.in.PageDTO;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllEmailsUseCaseImpl implements GetAllEmailsUseCase {

  private final EmailRepository emailRepository;

  @Override
  public List<EmailEntity> findAll(PageDTO page) {
    // inserir manipulação de dados/regras
    return emailRepository.findAll(page);
  }
}
