package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;
import java.util.Optional;
import java.util.UUID;

public interface GetEmailUseCase {

  Optional<EmailResponseDTO> findById(UUID emailId);
}
