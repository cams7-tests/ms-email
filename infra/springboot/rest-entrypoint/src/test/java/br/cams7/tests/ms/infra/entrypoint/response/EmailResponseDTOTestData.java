/** */
package br.cams7.tests.ms.infra.entrypoint.response;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID_UUID;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_SENT_DATE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_STATUS;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class EmailResponseDTOTestData {

  public static final String INVALID_EMAIL_ID_UUID = "0263d741-c187-4598-9b2e-8464a91bb78e";
  public static final String INVALID_EMAIL_ID_MONGO = "61e55ea4f01c694e18808764";
  public static final String INVALID_UUID = "fa3758635-fe93-4735-bca0-51e754833221";

  public static final String FIRST_EMAIL_ID_UUID = "fd0622c0-6101-11ec-902c-8f89d045b40c";
  public static final String FIRST_EMAIL_ID_MONGO = "61e2b7c6adf831316b1edba6";
  public static final String FIRST_EMAIL_IDENTIFICATION_NUMBER = "46384730081";
  public static final String FIRST_EMAIL_FROM = "from1@tests.cams7.br";
  public static final String FIRST_EMAIL_TO = "to1@tests.cams7.br";
  public static final String FIRST_EMAIL_SUBJECT = "E-mail subject 01";
  public static final String FIRST_EMAIL_TEXT = "E-mail message 01";

  public static final String LAST_EMAIL_ID_UUID = "86abce68-610b-11ec-8f23-733c774c16af";
  public static final String LAST_EMAIL_ID_MONGO = "61e2c0174f69e879a2201bfc";

  public static EmailResponseDTO getEmailResponseDTOWithUUID() {
    var email = new EmailResponseDTO();
    email.setEmailId(EMAIL_ID_UUID);
    email.setIdentificationNumber(OWNER_REF);
    email.setEmailFrom(EMAIL_FROM);
    email.setEmailTo(EMAIL_TO);
    email.setSubject(MESSAGE_SUBJECT);
    email.setText(MESSAGE_TEXT);
    email.setEmailSentDate(EMAIL_SENT_DATE);
    email.setEmailStatus(EMAIL_STATUS);
    return email;
  }

  public static LocalDateTime getLocalDateTime(String emailSentDate) {
    return LocalDateTime.ofInstant(
        LocalDateTime.parse(emailSentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .atZone(ZoneOffset.UTC)
            .toInstant(),
        ZoneOffset.of("-03:00"));
  }
}
