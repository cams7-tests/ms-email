package br.cams7.tests.ms.core.ports.in;

import br.cams7.tests.ms.core.domain.EmailEntity;

public interface SendEmailUseCase {

  EmailEntity sendEmail(EmailEntity email);
}
