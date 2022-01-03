package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.port.in.presenter.EmailResponseDTO;

public interface SendEmailDirectlyUseCase {

  EmailResponseDTO sendEmail(EmailVO email);
}
