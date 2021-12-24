/** */
package br.cams7.tests.ms.domain;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_ID;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_SENT_DATE;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_STATUS;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class EmailEntityTests {

  @Test
  void shouldCreateEmailInstanceWithSentStatus() {
    var email = defaultEmailEntity();

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
        defaultEmailEntity()
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

    var email = defaultEmailEntity();
    email.setEmailId(EMAIL_ID);
    email.setEmailStatus(EMAIL_STATUS);

    assertThat(email.getEmailId()).isEqualTo(EMAIL_ID);
    assertThat(email.getOwnerRef()).isEqualTo(OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EMAIL_STATUS);
  }
}
