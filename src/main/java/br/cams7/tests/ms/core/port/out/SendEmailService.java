package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;

public interface SendEmailService {

  void sendEmail(EmailEntity email) throws SendEmailException;
}
