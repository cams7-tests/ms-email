package br.cams7.tests.ms.core.ports.out;

import br.cams7.tests.ms.core.domain.EmailEntity;

public interface SendEmailService {

  void sendEmailSmtp(EmailEntity email);
}
