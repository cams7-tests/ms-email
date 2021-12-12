package br.cams7.tests.ms.core.ports.out;

import br.cams7.tests.ms.core.domain.EmailEntity;

public interface SendEmailServicePort {

  void sendEmailSmtp(EmailEntity email);
}
