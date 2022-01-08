package br.cams7.tests.ms.infra.mq;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;

public class EmailDTOTestData {
  public static EmailDTO getEmailDTO() {
    var email = new EmailDTO();
    email.setOwnerRef(OWNER_REF);
    email.setEmailFrom(EMAIL_FROM);
    email.setEmailTo(EMAIL_TO);
    email.setSubject(MESSAGE_SUBJECT);
    email.setText(MESSAGE_TEXT);
    return email;
  }
}
