/** */
package br.cams7.tests.ms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/** @author CIANDT\cmagalhaes */
public class EmailEntityTests {

  private static final EmailEntity DEFAULT_EMAIL = EmailEntityTestData.defaultEmail();

  @Test
  void sendEmailSuccess() {
    var email = DEFAULT_EMAIL;

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
  void sendEmailError() {
    var email = DEFAULT_EMAIL.withEmailStatus(EmailStatusEnum.ERROR);

    assertThat(email.getEmailId()).isEqualTo(EmailEntityTestData.EMAIL_ID);
    assertThat(email.getOwnerRef()).isEqualTo(EmailEntityTestData.OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EmailEntityTestData.EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EmailEntityTestData.EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(EmailEntityTestData.MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(EmailEntityTestData.MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EmailEntityTestData.EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EmailStatusEnum.ERROR);
  }

  @Test
  void sendEmailProcessing() {
    var email = DEFAULT_EMAIL.withEmailStatus(EmailStatusEnum.PROCESSING);

    assertThat(email.getEmailId()).isEqualTo(EmailEntityTestData.EMAIL_ID);
    assertThat(email.getOwnerRef()).isEqualTo(EmailEntityTestData.OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EmailEntityTestData.EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EmailEntityTestData.EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(EmailEntityTestData.MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(EmailEntityTestData.MESSAGE_TEXT);
    assertThat(email.getEmailSentDate()).isEqualTo(EmailEntityTestData.EMAIL_SENT_DATE);
    assertThat(email.getEmailStatus()).isEqualTo(EmailStatusEnum.PROCESSING);
  }
}
