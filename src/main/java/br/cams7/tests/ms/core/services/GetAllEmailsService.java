package br.cams7.tests.ms.core.services;


import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.domain.PageInfo;
import br.cams7.tests.ms.core.ports.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.ports.out.EmailRepositoryPort;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetAllEmailsService implements GetAllEmailsUseCase {

  private final EmailRepositoryPort emailRepositoryPort;

  @Override
  public List<EmailEntity> findAll(PageInfo pageInfo) {
    // inserir manipulação de dados/regras
    return emailRepositoryPort.findAll(pageInfo);
  }
}
