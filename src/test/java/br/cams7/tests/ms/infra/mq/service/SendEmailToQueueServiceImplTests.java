package br.cams7.tests.ms.infra.mq.service;

import static br.cams7.tests.ms.domain.EmailEntityTestData.defaultEmailEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.domain.EmailEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({SendEmailToQueueServiceImpl.class})
@TestPropertySource(properties = {"rabbitmq.queue=test", "rabbitmq.exchange=test"})
public class SendEmailToQueueServiceImplTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = defaultEmailEntity();
  private static final String QUEUE = "test";
  private static final String EXCHANGE = "test";
  private static final String ERROR_MESSAGE = "Error";

  @Autowired private SendEmailToQueueService sendEmailToQueueService;

  @MockBean private RabbitTemplate rabbitTemplate;

  @Test
  @DisplayName("sendEmail when successfull")
  void sendEmail_WhenSuccessful() {
    willDoNothing()
        .given(rabbitTemplate)
        .convertAndSend(anyString(), anyString(), any(Object.class));

    sendEmailToQueueService.sendEmail(DEFAULT_EMAIL_ENTITY);
    then(rabbitTemplate)
        .should(times(1))
        .convertAndSend(eq(EXCHANGE), eq(QUEUE), eq(DEFAULT_EMAIL_ENTITY));
  }

  @Test
  @DisplayName("sendEmail throws error when some error happened during send email")
  void sendEmail_ThrowsError_WhenSomeErrorHappenedDuringSendEmail() {
    willThrow(new RuntimeException(ERROR_MESSAGE))
        .given(rabbitTemplate)
        .convertAndSend(anyString(), anyString(), any(Object.class));

    var thrown =
        assertThrows(
            RuntimeException.class,
            () -> {
              sendEmailToQueueService.sendEmail(DEFAULT_EMAIL_ENTITY);
            });

    assertThat(thrown.getMessage()).isEqualTo(ERROR_MESSAGE);

    then(rabbitTemplate)
        .should(times(1))
        .convertAndSend(eq(EXCHANGE), eq(QUEUE), eq(DEFAULT_EMAIL_ENTITY));
  }
}
