package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.domain.EmailEntity;
import java.util.Optional;
import java.util.UUID;

public interface GetEmailRepository {

  Optional<EmailEntity> findById(UUID emailId);
}
