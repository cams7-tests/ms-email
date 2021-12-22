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

public class EmailVOTests {

  private static final EmailVO DEFAULT_EMAIL_VO = EmailVOTestData.defaultEmail();
  private static final String EMPTY = "   ";
  private static final String WRONG_IDENTIFICATION_NUMBER = "56317992001";
  private static final String WRONG_EMAIL = "test";

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
  @DisplayName("create email instance throws error when pass null \"identification number\"")
  void createEmailInstance_ThrowsError_WhenPassNullIdentificationNumber() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(null, EMAIL_FROM, EMAIL_TO, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).isEqualTo("identificationNumber: must not be blank");
  }

  @Test
  @DisplayName("create email instance throws error when pass empty \"identification number\"")
  void createEmailInstance_ThrowsError_WhenPassEmptyIdentificationNumber() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(EMPTY, EMAIL_FROM, EMAIL_TO, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).contains("identificationNumber: must not be blank");
    assertThat(thrown.getMessage())
        .contains(
            "identificationNumber: invalid Brazilian individual taxpayer registry number (CPF)");
  }

  @Test
  @DisplayName("create email instance throws error when pass wrong \"identification number\"")
  void createEmailInstance_ThrowsError_WhenPassWrongIdentificationNumber() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(
                  WRONG_IDENTIFICATION_NUMBER, EMAIL_FROM, EMAIL_TO, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage())
        .isEqualTo(
            "identificationNumber: invalid Brazilian individual taxpayer registry number (CPF)");
  }

  @Test
  @DisplayName("create email instance throws error when pass null \"email from\"")
  void createEmailInstance_ThrowsError_WhenPassNullEmailFrom() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, null, EMAIL_TO, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).isEqualTo("emailFrom: must not be blank");
  }

  @Test
  @DisplayName("create email instance throws error when pass empty \"email from\"")
  void createEmailInstance_ThrowsError_WhenPassEmptyEmailFrom() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMPTY, EMAIL_TO, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).contains("emailFrom: must not be blank");
    assertThat(thrown.getMessage()).contains("emailFrom: must be a well-formed email address");
  }

  @Test
  @DisplayName("create email instance throws error when pass wrong \"email from\"")
  void createEmailInstance_ThrowsError_WhenPassWrongEmailFrom() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, WRONG_EMAIL, EMAIL_TO, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).isEqualTo("emailFrom: must be a well-formed email address");
  }

  @Test
  @DisplayName("create email instance throws error when pass null \"email to\"")
  void createEmailInstance_ThrowsError_WhenPassNullEmailTo() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMAIL_FROM, null, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).isEqualTo("emailTo: must not be blank");
  }

  @Test
  @DisplayName("create email instance throws error when pass empty \"email to\"")
  void createEmailInstance_ThrowsError_WhenPassEmptyEmailTo() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMAIL_FROM, EMPTY, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).contains("emailTo: must not be blank");
    assertThat(thrown.getMessage()).contains("emailTo: must be a well-formed email address");
  }

  @Test
  @DisplayName("create email instance throws error when pass wrong \"email to\"")
  void createEmailInstance_ThrowsError_WhenPassWrongEmailTo() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMAIL_FROM, WRONG_EMAIL, MESSAGE_SUBJECT, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).isEqualTo("emailTo: must be a well-formed email address");
  }

  @Test
  @DisplayName("create email instance throws error when pass null subject")
  void createEmailInstance_ThrowsError_WhenPassNullSubject() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMAIL_FROM, EMAIL_TO, null, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).isEqualTo("subject: must not be blank");
  }

  @Test
  @DisplayName("create email instance throws error when pass empty subject")
  void createEmailInstance_ThrowsError_WhenPassEmptySubject() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMAIL_FROM, EMAIL_TO, EMPTY, MESSAGE_TEXT);
            });

    assertThat(thrown.getMessage()).isEqualTo("subject: must not be blank");
  }

  @Test
  @DisplayName("create email instance throws error when pass null text")
  void createEmailInstance_ThrowsError_WhenPassNullText() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMAIL_FROM, EMAIL_TO, MESSAGE_SUBJECT, null);
            });

    assertThat(thrown.getMessage()).isEqualTo("text: must not be blank");
  }

  @Test
  @DisplayName("create email instance throws error when pass empty text")
  void createEmailInstance_ThrowsError_WhenPassEmptyText() {
    ConstraintViolationException thrown =
        assertThrows(
            ConstraintViolationException.class,
            () -> {
              new EmailVO(OWNER_REF, EMAIL_FROM, EMAIL_TO, MESSAGE_SUBJECT, EMPTY);
            });

    assertThat(thrown.getMessage()).isEqualTo("text: must not be blank");
  }
}
