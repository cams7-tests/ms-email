/** */
package br.cams7.tests.ms.infra.entrypoint.response;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_SENT_DATE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_STATUS;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class EmailResponseDTOTestData {

  public static final String INVALID_EMAIL_ID = "0263d741-c187-4598-9b2e-8464a91bb78e";
  public static final String INVALID_UUID = "fa3758635-fe93-4735-bca0-51e754833221";

  public static final String FIRST_EMAIL_ID = "fd0622c0-6101-11ec-902c-8f89d045b40c";
  public static final String FIRST_EMAIL_IDENTIFICATION_NUMBER = "46384730081";
  public static final String FIRST_EMAIL_FROM = "from1@tests.cams7.br";
  public static final String FIRST_EMAIL_TO = "to1@tests.cams7.br";
  public static final String FIRST_EMAIL_SUBJECT = "E-mail subject 01";
  public static final String FIRST_EMAIL_TEXT = "E-mail message 01";
  public static final EmailResponseDTO FIRST_EMAIL_RESPONSE_DTO = getFistEmailResponseDTO();

  public static final String LAST_EMAIL_ID = "86abce68-610b-11ec-8f23-733c774c16af";
  public static final EmailResponseDTO LAST_EMAIL_RESPONSE_DTO = getLastEmailResponseDTOEmail();

  public static EmailResponseDTO getEmailResponseDTO() {
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

  private static EmailResponseDTO getFistEmailResponseDTO() {
    var email = new EmailResponseDTO();
    email.setEmailId(UUID.fromString(FIRST_EMAIL_ID));
    email.setIdentificationNumber(FIRST_EMAIL_IDENTIFICATION_NUMBER);
    email.setEmailFrom(FIRST_EMAIL_FROM);
    email.setEmailTo(FIRST_EMAIL_TO);
    email.setEmailSentDate(getLocalDateTime("2022-01-07 00:27:30"));
    email.setEmailStatus(EmailStatusEnum.SENT);
    email.setSubject(FIRST_EMAIL_SUBJECT);
    email.setText(FIRST_EMAIL_TEXT);
    return email;
  }

  private static EmailResponseDTO getLastEmailResponseDTOEmail() {
    var email = new EmailResponseDTO();
    email.setEmailId(UUID.fromString(LAST_EMAIL_ID));
    email.setIdentificationNumber("90978373081");
    email.setEmailFrom("from3@tests.cams7.br");
    email.setEmailTo("to1@tests.cams7.br");
    email.setEmailSentDate(getLocalDateTime("2021-12-23 17:43:15"));
    email.setEmailStatus(EmailStatusEnum.SENT);
    email.setSubject("E-mail subject 11");
    email.setText("E-mail message 11");
    return email;
  }

  private static LocalDateTime getLocalDateTime(String emailSentDate) {
    return LocalDateTime.ofInstant(
        LocalDateTime.parse(emailSentDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            .atZone(ZoneOffset.UTC)
            .toInstant(),
        ZoneOffset.of("-03:00"));
  }
}
