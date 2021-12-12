package br.cams7.tests.ms.core.ports.in;

import br.cams7.tests.ms.core.domain.EmailEntity;
import java.util.Optional;
import java.util.UUID;

public interface GetEmailUseCase {

  Optional<EmailEntity> findById(UUID emailId);
}
