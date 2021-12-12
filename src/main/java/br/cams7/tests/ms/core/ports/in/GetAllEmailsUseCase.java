package br.cams7.tests.ms.core.ports.in;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.domain.PageInfo;
import java.util.List;

public interface GetAllEmailsUseCase {

  List<EmailEntity> findAll(PageInfo pageInfo);
}
