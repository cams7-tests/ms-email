package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;

public interface SendEmailService {

  void sendEmail(EmailEntity email) throws SendEmailException;
}
