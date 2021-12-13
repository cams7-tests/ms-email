package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.PageDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailRepository {

  EmailEntity save(EmailEntity email);

  List<EmailEntity> findAll(PageDTO page);

  Optional<EmailEntity> findById(UUID emailId);
}