package br.cams7.tests.ms.core.port.out;

import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;

@FunctionalInterface
public interface SendEmailGateway {

  void sendEmail(EmailEntity email) throws SendEmailException;
}
