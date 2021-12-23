/** */
package br.cams7.tests.ms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/** @author CIANDT\cmagalhaes */
class EmailEntityTests {

  @Test
  void shouldCreateEmailInstanceWithSentStatus() {
    var email = EmailEntityTestData.defaultEmail();

    assertThat(email.getEmailId()).isEqualTo(EmailEntityTestData.EMAIL_ID);
    assertThat(email.getOwnerRef()).isEqualTo(EmailEntityTestData.OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EmailEntityTestData.EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EmailEntityTestData.EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(EmailEntityTestData.MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(EmailEntityTestData.MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EmailEntityTestData.EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EmailEntityTestData.EMAIL_STATUS);
  }

  @Test
  void shouldCreateEmailInstanceWithErrorStatus() {
    final var EMAIL_ID = UUID.randomUUID();
    final var OWNER_REF = "06744839012";
    final var EMAIL_FROM = "from2@tests.cams7.br";
    final var EMAIL_TO = "to2@tests.cams7.br";
    final var MESSAGE_SUBJECT = "Second message subject";
    final var MESSAGE_TEXT = "Second message text";
    final var EMAIL_SENT_DATE =
        LocalDateTime.parse(
            "2022-01-01 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    final var EMAIL_STATUS = EmailStatusEnum.ERROR;

    var email =
        EmailEntityTestData.defaultEmail()
            .withEmailId(EMAIL_ID)
            .withOwnerRef(OWNER_REF)
            .withEmailFrom(EMAIL_FROM)
            .withEmailTo(EMAIL_TO)
            .withSubject(MESSAGE_SUBJECT)
            .withText(MESSAGE_TEXT)
            .withEmailSentDate(EMAIL_SENT_DATE)
            .withEmailStatus(EMAIL_STATUS);

    assertThat(email.getEmailId()).isEqualTo(EMAIL_ID);
    assertThat(email.getOwnerRef()).isEqualTo(OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EMAIL_STATUS);
  }

  @Test
  void shouldCreateEmailInstanceWithProcessingStatus() {
    final var EMAIL_ID = UUID.randomUUID();
    final var EMAIL_STATUS = EmailStatusEnum.PROCESSING;

    var email = EmailEntityTestData.defaultEmail();
    email.setEmailId(EMAIL_ID);
    email.setEmailStatus(EMAIL_STATUS);

    assertThat(email.getEmailId()).isEqualTo(EMAIL_ID);
    assertThat(email.getOwnerRef()).isEqualTo(EmailEntityTestData.OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EmailEntityTestData.EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EmailEntityTestData.EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(EmailEntityTestData.MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(EmailEntityTestData.MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EmailEntityTestData.EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EMAIL_STATUS);
  }
}
