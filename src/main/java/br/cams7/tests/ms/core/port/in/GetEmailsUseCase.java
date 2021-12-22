package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;

public interface GetEmailsUseCase {

  List<EmailEntity> findAll(PageDTO page);
}
