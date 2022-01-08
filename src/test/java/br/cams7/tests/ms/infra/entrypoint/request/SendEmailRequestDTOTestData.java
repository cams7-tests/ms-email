package br.cams7.tests.ms.infra.entrypoint.request;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;

public final class SendEmailRequestDTOTestData {

  public static final SendEmailRequestDTO NEW_EMAIL = getSendEmailRequestDTO();
  public static final SendEmailRequestDTO NEW_EMAIL_WITH_EMPTY_SUBJECT =
      getNewEmailWithEmptySubject();
  public static final SendEmailRequestDTO NEW_EMAIL_WITH_INVALID_IDENTIFICATION_NUMBER =
      getNewEmailWithInvalidIdentificationNumber();
  public static final SendEmailRequestDTO NEW_EMAIL_WITH_INVALID_EMAIL_FROM =
      getNewEmailWithInvalidEmailFrom();

  public static SendEmailRequestDTO getSendEmailRequestDTO() {
    var email = new SendEmailRequestDTO();
    email.setIdentificationNumber(OWNER_REF);
    email.setEmailFrom(EMAIL_FROM);
    email.setEmailTo(EMAIL_TO);
    email.setSubject(MESSAGE_SUBJECT);
    email.setText(MESSAGE_TEXT);
    return email;
  }

  private static SendEmailRequestDTO getNewEmailWithEmptySubject() {
    var email = getSendEmailRequestDTO();
    email.setSubject("");
    return email;
  }

  private static SendEmailRequestDTO getNewEmailWithInvalidIdentificationNumber() {
    var email = getSendEmailRequestDTO();
    email.setIdentificationNumber("12345678901");
    return email;
  }

  private static SendEmailRequestDTO getNewEmailWithInvalidEmailFrom() {
    var email = getSendEmailRequestDTO();
    email.setEmailFrom("test");
    return email;
  }
}
