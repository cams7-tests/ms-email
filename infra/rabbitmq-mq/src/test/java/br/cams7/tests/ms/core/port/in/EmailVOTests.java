/** */
package br.cams7.tests.ms.core.port.in;

import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_FROM;
import static br.cams7.tests.ms.domain.EmailEntityTestData.EMAIL_TO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_SUBJECT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.MESSAGE_TEXT;
import static br.cams7.tests.ms.domain.EmailEntityTestData.OWNER_REF;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EmailVOTests {

  private static final EmailVO DEFAULT_EMAIL_VO = EmailVOTestData.getEmailVO();
  private static final String EMPTY = "   ";
  private static final String WRONG_IDENTIFICATION_NUMBER = "56317992001";
  private static final String WRONG_EMAIL = "test";

  private static final String ERROR_MESSAGE_BLANK_IDENTIFICATION_NUMBER =
      "identificationNumber: must not be blank";
  private static final String ERROR_MESSAGE_WRONG_IDENTIFICATION_NUMBER =
      "identificationNumber: invalid Brazilian individual taxpayer registry number (CPF)";
  private static final String ERROR_MESSAGE_BLANK_EMAIL_FROM = "emailFrom: must not be blank";
  private static final String ERROR_MESSAGE_WRONG_EMAIL_FROM =
      "emailFrom: must be a well-formed email address";
  private static final String ERROR_MESSAGE_BLANK_EMAIL_TO = "emailTo: must not be blank";
  private static final String ERROR_MESSAGE_WRONG_EMAIL_TO =
      "emailTo: must be a well-formed email address";
  private static final String ERROR_MESSAGE_BLANK_SUBJECT = "subject: must not be blank";
  private static final String ERROR_MESSAGE_BLANK_TEXT = "text: must not be blank";

  @Test
  @DisplayName("create email instance when successfull")
  void createEmailInstance_WhenSuccessful() {
    var email = DEFAULT_EMAIL_VO;

    assertThat(email).isNotNull();
    assertThat(email.getIdentificationNumber()).isEqualTo(OWNER_REF);
    assertThat(email.getEmailFrom()).isEqualTo(EMAIL_FROM);
    assertThat(email.getEmailTo()).isEqualTo(EMAIL_TO);
    assertThat(email.getSubject()).isEqualTo(MESSAGE_SUBJECT);
    assertThat(email.getText()).isEqualTo(MESSAGE_TEXT);
  }

  @Test
  @DisplayName("create email instance throws error when pass null params")
  void createEmailInstance_ThrowsError_WhenPassNullParams() {
    var thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(null, null, null, null, null);
            });

    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_IDENTIFICATION_NUMBER);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_EMAIL_FROM);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_EMAIL_TO);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_SUBJECT);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_TEXT);
  }

  @Test
  @DisplayName("create email instance throws error when pass empty params")
  void createEmailInstance_ThrowsError_WhenPassEmptyParams() {
    var thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY);
            });

    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_IDENTIFICATION_NUMBER);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_EMAIL_FROM);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_EMAIL_TO);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_SUBJECT);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_TEXT);
  }

  @Test
  @DisplayName("create email instance throws error when pass wrong params")
  void createEmailInstance_ThrowsError_WhenPassWrongParams() {
    var thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(WRONG_IDENTIFICATION_NUMBER, WRONG_EMAIL, WRONG_EMAIL, EMPTY, EMPTY);
            });

    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_WRONG_IDENTIFICATION_NUMBER);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_WRONG_EMAIL_FROM);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_WRONG_EMAIL_TO);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_SUBJECT);
    assertThat(thrown.getMessage()).contains(ERROR_MESSAGE_BLANK_TEXT);
  }
}
