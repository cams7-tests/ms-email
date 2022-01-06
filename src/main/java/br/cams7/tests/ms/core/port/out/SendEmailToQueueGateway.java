package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.domain.EmailEntity;

public interface SendEmailToQueueGateway {

  void sendEmail(EmailEntity email);
}
