/** */
package br.cams7.tests.ms.infra.smtp;

import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static br.cams7.tests.ms.infra.smtp.SimpleMailMessageTestData.defaultSimpleMailMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({SMTPSendEmailService.class})
class SMTPSendEmailServiceTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final SimpleMailMessage DEFAULT_SIMPLE_MAIL_MESSAGE = defaultSimpleMailMessage();
  private static final ArgumentCaptor<SimpleMailMessage> SIMPLE_MAIL_MESSAGE_CAPTOR =
      ArgumentCaptor.forClass(SimpleMailMessage.class);
  private static final String ERROR_MESSAGE = "Error";

  @Autowired private SendEmailService sendEmailService;

  @MockBean private JavaMailSender emailSender;

  @BeforeEach
  void setUp() {
    willDoNothing().given(emailSender).send(any(SimpleMailMessage.class));
  }

  @Test
  @DisplayName("sendEmail when successfull")
  void sendEmail_WhenSuccessful() throws SendEmailException {
    sendEmailService.sendEmail(DEFAULT_EMAIL_ENTITY);

    then(emailSender).should(times(1)).send(SIMPLE_MAIL_MESSAGE_CAPTOR.capture());
    assertThat(SIMPLE_MAIL_MESSAGE_CAPTOR.getValue()).isEqualTo(DEFAULT_SIMPLE_MAIL_MESSAGE);
  }

  @Test
  @DisplayName("sendEmail throws error when pass null email entity")
  void sendEmail_ThrowsError_WhenPassNullEmailEntity() {
    assertThrows(
        NullPointerException.class,
        () -> {
          sendEmailService.sendEmail(null);
        });

    then(emailSender).should(times(0)).send(any(SimpleMailMessage.class));
  }

  @Test
  @DisplayName("sendEmail throws error when some error happened during send email")
  void sendEmail_ThrowsError_WhenSomeErrorHappenedDuringSendEmail() {
    willThrow(
            new MailException(ERROR_MESSAGE) {
              private static final long serialVersionUID = 1L;
            })
        .given(emailSender)
        .send(any(SimpleMailMessage.class));

    var thrown =
        assertThrows(
            SendEmailException.class,
            () -> {
              sendEmailService.sendEmail(DEFAULT_EMAIL_ENTITY);
            });

    assertThat(thrown.getMessage()).isEqualTo(ERROR_MESSAGE);

    then(emailSender).should(times(1)).send(SIMPLE_MAIL_MESSAGE_CAPTOR.capture());
    assertThat(SIMPLE_MAIL_MESSAGE_CAPTOR.getValue()).isEqualTo(DEFAULT_SIMPLE_MAIL_MESSAGE);
  }
}
