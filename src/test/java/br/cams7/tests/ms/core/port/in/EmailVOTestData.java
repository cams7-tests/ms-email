package br.cams7.tests.ms.core.port.in;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;

public class EmailVOTestData {

  public static EmailVO getEmailVO() {
    return new EmailVO(OWNER_REF, EMAIL_FROM, EMAIL_TO, MESSAGE_SUBJECT, MESSAGE_TEXT);
  }
}
