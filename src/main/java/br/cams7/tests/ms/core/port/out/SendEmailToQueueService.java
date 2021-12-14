package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.domain.EmailEntity;

public interface SendEmailToQueueService {

  void sendEmail(EmailEntity email);
}
