package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.domain.EmailEntity;
import java.util.List;

public interface GetAllEmailsUseCase {

  List<EmailEntity> findAll(PageDTO page);
}
