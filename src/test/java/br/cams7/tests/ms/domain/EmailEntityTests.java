/** */
package br.cams7.tests.ms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.Test;

/** @author CIANDT\cmagalhaes */
public class EmailEntityTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = EmailEntityTestData.defaultEmail();

  @Test
  void createDefaultInstance() {
    var email = DEFAULT_EMAIL_ENTITY;

    assertThat(email.getEmailId()).isEqualTo(EmailEntityTestData.EMAIL_ID);
    assertThat(email.getOwnerRef()).isEqualTo(EmailEntityTestData.OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EmailEntityTestData.EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EmailEntityTestData.EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(EmailEntityTestData.MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(EmailEntityTestData.MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EmailEntityTestData.EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EmailEntityTestData.EMAIL_STATUS);
    assertThat(email.toString()).isNotBlank();
    assertThat(email.hashCode()).isNotNull();
    assertThat(email.equals(DEFAULT_EMAIL_ENTITY)).isTrue();
    assertThat(email.canEqual(DEFAULT_EMAIL_ENTITY)).isTrue();
  }

  @Test
  void CreateSecondAlteredInstance() {
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
        DEFAULT_EMAIL_ENTITY
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
    assertThat(email.equals(DEFAULT_EMAIL_ENTITY)).isFalse();
    assertThat(email.equals(email.withEmailId(EmailEntityTestData.EMAIL_ID))).isFalse();
    assertThat(email.equals(email.withOwnerRef(EmailEntityTestData.OWNER_REF))).isFalse();
    assertThat(email.equals(email.withEmailFrom(EmailEntityTestData.EMAIL_FROM))).isFalse();
    assertThat(email.equals(email.withEmailTo(EmailEntityTestData.EMAIL_TO))).isFalse();
    assertThat(email.equals(email.withSubject(EmailEntityTestData.MESSAGE_SUBJECT))).isFalse();
    assertThat(email.equals(email.withText(EmailEntityTestData.MESSAGE_TEXT))).isFalse();
    assertThat(email.equals(email.withEmailSentDate(EmailEntityTestData.EMAIL_SENT_DATE)))
        .isFalse();
    assertThat(email.equals(email.withEmailStatus(EmailEntityTestData.EMAIL_STATUS))).isFalse();
    assertThat(email.equals(null)).isFalse();
    assertThat(email.equals("")).isFalse();
  }

  @Test
  void CreateThirdAlteredInstance() {
    final var EMAIL_ID = UUID.randomUUID();
    final var EMAIL_STATUS = EmailStatusEnum.PROCESSING;

    var email = DEFAULT_EMAIL_ENTITY;
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
    assertThat(email.equals(DEFAULT_EMAIL_ENTITY)).isTrue();
  }
}
