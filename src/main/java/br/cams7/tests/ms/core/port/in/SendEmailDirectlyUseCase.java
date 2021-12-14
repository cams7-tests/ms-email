package br.cams7.tests.ms.core.port.in;

import br.cams7.tests.ms.core.domain.EmailEntity;

public interface SendEmailDirectlyUseCase {

  EmailEntity sendEmail(EmailVO email);
}
