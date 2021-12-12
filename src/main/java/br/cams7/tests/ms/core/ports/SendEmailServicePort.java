package br.cams7.tests.ms.core.ports;

import br.cams7.tests.ms.core.domain.EmailEntity;

public interface SendEmailServicePort {

  void sendEmailSmtp(EmailEntity email);
}
