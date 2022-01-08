package br.cams7.tests.ms.infra.entrypoint;

import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import br.cams7.tests.ms.infra.entrypoint.response.EmailResponseDTO;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class EmailResponseCreator {

  public static final String INVALID_EMAIL_ID = "fa3758635-fe93-4735-bca0-51e754833221";

  public static final String FIRST_EMAIL_ID = "fd0622c0-6101-11ec-902c-8f89d045b40c";
  public static final String FIRST_EMAIL_IDENTIFICATION_NUMBER = "46384730081";
  public static final String FIRST_EMAIL_FROM = "from1@tests.cams7.br";
  public static final String FIRST_EMAIL_TO = "to1@tests.cams7.br";
  public static final String FIRST_EMAIL_SUBJECT = "E-mail subject 01";
  public static final String FIRST_EMAIL_TEXT = "E-mail message 01";
  public static final EmailResponseDTO FIRST_EMAIL_RESPONSE_DTO = getFistEmailResponseDTO();

  public static final String LAST_EMAIL_ID = "86abce68-610b-11ec-8f23-733c774c16af";
  public static final EmailResponseDTO LAST_EMAIL_RESPONSE_DTO = getLastEmailResponseDTOEmail();

  public static final String NEW_EMAIL_IDENTIFICATION_NUMBER = "45537498073";
  public static final String NEW_EMAIL_FROM = "from@tests.cams7.br";
  public static final String NEW_EMAIL_TO = "to@tests.cams7.br";
  public static final String NEW_EMAIL_SUBJECT = "E-mail subject";
  public static final String NEW_EMAIL_TEXT = "E-mail message";
  public static final EmailResponseDTO NEW_EMAIL_RESPONSE_DTO = getNewEmailResponseDTO();
  public static final EmailEntity NEW_EMAIL_ENTITY = getNewEmailEntity();

  public static final EmailResponseDTO NEW_EMAIL_WITH_EMPTY_SUBJECT = getNewEmailWithEmptySubject();

  public static final EmailResponseDTO NEW_EMAIL_WITH_INVALID_IDENTIFICATION_NUMBER =
      getNewEmailWithInvalidIdentificationNumber();

  public static final EmailResponseDTO NEW_EMAIL_WITH_INVALID_EMAIL_FROM =
      getNewEmailWithInvalidEmailFrom();

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

  private static EmailResponseDTO getNewEmailResponseDTO() {
    var email = new EmailResponseDTO();
    email.setIdentificationNumber(NEW_EMAIL_IDENTIFICATION_NUMBER);
    email.setEmailFrom(NEW_EMAIL_FROM);
    email.setEmailTo(NEW_EMAIL_TO);
    email.setSubject(NEW_EMAIL_SUBJECT);
    email.setText(NEW_EMAIL_TEXT);
    return email;
  }

  public static EmailEntity getNewEmailEntity() {
    var email = new EmailEntity();
    email.setOwnerRef(NEW_EMAIL_IDENTIFICATION_NUMBER);
    email.setEmailFrom(NEW_EMAIL_FROM);
    email.setEmailTo(NEW_EMAIL_TO);
    email.setSubject(NEW_EMAIL_SUBJECT);
    email.setText(NEW_EMAIL_TEXT);
    email.setEmailStatus(EmailStatusEnum.SENT);
    return email;
  }

  public static EmailResponseDTO getNewEmailWithEmptySubject() {
    var email = getNewEmailResponseDTO();
    email.setSubject("");
    return email;
  }

  public static EmailResponseDTO getNewEmailWithInvalidIdentificationNumber() {
    var email = getNewEmailResponseDTO();
    email.setIdentificationNumber("12345678901");
    return email;
  }

  public static EmailResponseDTO getNewEmailWithInvalidEmailFrom() {
    var email = getNewEmailResponseDTO();
    email.setEmailFrom("test");
    ;
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
