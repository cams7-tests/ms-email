/** */
package br.cams7.tests.ms.core.port.in.presenter;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_SENT_DATE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_STATUS;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;

import br.cams7.tests.ms.infra.entrypoint.EmailResponseDTO;

public final class EmailResponseDTOTestData {

  public static EmailResponseDTO defaultEmailResponseDTO() {
    var email = new EmailResponseDTO();
    email.setEmailId(EMAIL_ID);
    email.setIdentificationNumber(OWNER_REF);
    email.setEmailFrom(EMAIL_FROM);
    email.setEmailTo(EMAIL_TO);
    email.setSubject(MESSAGE_SUBJECT);
    email.setText(MESSAGE_TEXT);
    email.setEmailSentDate(EMAIL_SENT_DATE);
    email.setEmailStatus(EMAIL_STATUS);

    return email;
  }
}
