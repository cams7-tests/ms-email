package br.cams7.tests.ms.infra.entrypoint;

import br.cams7.tests.ms.domain.EmailStatusEnum;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class EmailResponseCreator {

  public static final String INVALID_EMAIL_ID = "fa3758635-fe93-4735-bca0-51e754833221";
  public static final String FIRST_EMAIL_ID = "fd0622c0-6101-11ec-902c-8f89d045b40c";
  public static final EmailResponseDTO FIRST_EMAIL = getFistEmail();
  public static final String LAST_EMAIL_ID = "86abce68-610b-11ec-8f23-733c774c16af";
  public static final EmailResponseDTO LAST_EMAIL = getLastEmail();

  public static EmailResponseDTO getFistEmail() {
    var email = new EmailResponseDTO();
    email.setEmailId(UUID.fromString(FIRST_EMAIL_ID));
    email.setIdentificationNumber("46384730081");
    email.setEmailFrom("from1@tests.cams7.br");
    email.setEmailTo("to1@tests.cams7.br");
    email.setEmailSentDate(getLocalDateTime("2022-01-07 00:27:30"));
    email.setEmailStatus(EmailStatusEnum.SENT);
    email.setSubject("E-mail subject 01");
    email.setText("E-mail message 01");
    return email;
  }

  public static EmailResponseDTO getLastEmail() {
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
