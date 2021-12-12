package br.cams7.tests.ms.core.ports;

import br.cams7.tests.ms.core.domain.Email;
import br.cams7.tests.ms.core.domain.PageInfo;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmailServicePort {

  Email sendEmail(Email email);

  List<Email> findAll(PageInfo pageInfo);

  Optional<Email> findById(UUID emailId);

  Email save(Email email);
}