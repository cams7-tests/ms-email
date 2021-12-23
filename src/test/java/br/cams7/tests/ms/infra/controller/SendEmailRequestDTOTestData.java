package br.cams7.tests.ms.infra.controller;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;

public class SendEmailRequestDTOTestData {

  public static SendEmailRequestDTO defaultEmail() {
    var request = new SendEmailRequestDTO();
    request.setIdentificationNumber(OWNER_REF);
    request.setEmailFrom(EMAIL_FROM);
    request.setEmailTo(EMAIL_TO);
    request.setSubject(MESSAGE_SUBJECT);
    request.setText(MESSAGE_TEXT);
    return request;
  }
}
