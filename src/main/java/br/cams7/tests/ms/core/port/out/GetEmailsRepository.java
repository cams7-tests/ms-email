package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.common.PageDTO;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.List;

public interface GetEmailsRepository {

  List<EmailEntity> findAll(PageDTO page);
}
