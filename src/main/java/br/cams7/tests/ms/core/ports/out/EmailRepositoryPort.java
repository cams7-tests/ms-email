package br.cams7.tests.ms.core.ports.out;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.domain.PageInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailRepositoryPort {

  EmailEntity save(EmailEntity email);

  List<EmailEntity> findAll(PageInfo pageInfo);

  Optional<EmailEntity> findById(UUID emailId);
}
