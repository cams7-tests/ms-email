package br.cams7.tests.ms.core.ports;

import br.cams7.tests.ms.core.domain.Email;

public interface SendEmailServicePort {

  void sendEmailSmtp(Email email);
}
