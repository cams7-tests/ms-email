package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.domain.EmailEntity;

public interface SendEmailService {

  void sendEmail(EmailEntity email);
}
