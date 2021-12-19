package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.PageDTO;
import java.util.List;

public interface GetAllEmailsUseCase {

  List<EmailEntity> findAll(PageDTO page);
}
