package br.cams7.tests.ms.infra.mq;

import static br.cams7.tests.ms.core.port.in.EmailVOTestData.defaultEmailVO;
import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static br.cams7.tests.ms.infra.mq.EmailDTOTestData.defaultEmailDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.domain.EmailEntity;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

@ExtendWith(MockitoExtension.class)
class EmailConsumerTests {

  private static final EmailDTO DEFAULT_EMAIL_DTO = defaultEmailDTO();
  private static final EmailVO DEFAULT_EMAIL_VO = defaultEmailVO();
  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();

  @InjectMocks private EmailConsumer emailConsumer;

  @Mock private SendEmailDirectlyUseCase sendEmailUseCase;

  @Captor private ArgumentCaptor<EmailVO> emailVOCaptor;

  @Test
  @DisplayName("listen when successfull")
  void listen_WhenSuccessful() {

    given(sendEmailUseCase.sendEmail(any(EmailVO.class))).willReturn(DEFAULT_EMAIL_ENTITY);

    emailConsumer.listen(DEFAULT_EMAIL_DTO);

    then(sendEmailUseCase).should().sendEmail(emailVOCaptor.capture());

    assertThat(emailVOCaptor.getValue()).isEqualTo(DEFAULT_EMAIL_VO);
  }

  @Test
  @DisplayName("listen throws error when pass null email dto")
  void listen_ThrowsError_WhenPassNullEmailDTO() {
    assertThrows(
        NullPointerException.class,
        () -> {
          emailConsumer.listen(null);
        });

    then(sendEmailUseCase).should(never()).sendEmail(any(EmailVO.class));
  }

  @Test
  @DisplayName("listen throws error when some error happened during send email")
  void listen_ThrowsError_WhenSomeErrorHappenedDuringSendEmail() {
    willThrow(ConstraintViolationException.class)
        .given(sendEmailUseCase)
        .sendEmail(any(EmailVO.class));

    assertThrows(
        AmqpRejectAndDontRequeueException.class,
        () -> {
          emailConsumer.listen(DEFAULT_EMAIL_DTO);
        });

    then(sendEmailUseCase).should().sendEmail(emailVOCaptor.capture());
    assertThat(emailVOCaptor.getValue()).isEqualTo(DEFAULT_EMAIL_VO);
  }
}
