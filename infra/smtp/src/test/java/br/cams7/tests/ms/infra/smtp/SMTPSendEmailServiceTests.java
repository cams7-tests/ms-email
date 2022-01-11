/** */
package br.cams7.tests.ms.infra.smtp;

import static br.cams7.tests.ms.domain.EmailEntityTestData.getEmailEntity;
import static br.cams7.tests.ms.infra.smtp.SimpleMailMessageTestData.getSimpleMailMessage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.port.out.exception.SendEmailException;
import br.cams7.tests.ms.domain.EmailEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class SMTPSendEmailServiceTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = getEmailEntity();
  private static final SimpleMailMessage DEFAULT_SIMPLE_MAIL_MESSAGE = getSimpleMailMessage();
  private static final String ERROR_MESSAGE = "Error";

  @InjectMocks private SMTPSendEmailService sendEmailService;

  @Mock private JavaMailSender emailSender;

  @Captor private ArgumentCaptor<SimpleMailMessage> simpleMailMessageCaptor;

  @Test
  @DisplayName("sendEmail when successfull")
  void sendEmail_WhenSuccessful() throws SendEmailException {

    willDoNothing().given(emailSender).send(any(SimpleMailMessage.class));

    sendEmailService.sendEmail(DEFAULT_EMAIL_ENTITY);

    then(emailSender).should(times(1)).send(simpleMailMessageCaptor.capture());
    assertThat(simpleMailMessageCaptor.getValue()).isEqualTo(DEFAULT_SIMPLE_MAIL_MESSAGE);
  }

  @Test
  @DisplayName("sendEmail throws error when pass null email entity")
  void sendEmail_ThrowsError_WhenPassNullEmailEntity() {
    assertThrows(
        NullPointerException.class,
        () -> {
          sendEmailService.sendEmail(null);
        });

    then(emailSender).should(never()).send(any(SimpleMailMessage.class));
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

    then(emailSender).should(times(1)).send(simpleMailMessageCaptor.capture());
    assertThat(simpleMailMessageCaptor.getValue()).isEqualTo(DEFAULT_SIMPLE_MAIL_MESSAGE);
  }
}
