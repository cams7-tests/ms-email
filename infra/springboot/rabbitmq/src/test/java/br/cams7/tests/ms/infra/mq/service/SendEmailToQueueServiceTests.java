package br.cams7.tests.ms.infra.mq.service;

import static br.cams7.tests.ms.domain.EmailEntityTestData.getEmailEntity;
import static br.cams7.tests.ms.domain.EmailStatusEnum.SENT;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static reactor.test.StepVerifier.create;

import br.cams7.tests.ms.core.port.out.SendEmailToQueueGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.blockhound.BlockHound;
import reactor.blockhound.BlockingOperationError;
import reactor.core.scheduler.Schedulers;

@ExtendWith(SpringExtension.class)
@Import({SendEmailToQueueService.class})
@TestPropertySource(properties = {"rabbitmq.queue=test", "rabbitmq.exchange=test"})
class SendEmailToQueueServiceTests {

  private static final EmailEntity DEFAULT_EMAIL_ENTITY = getEmailEntity();
  private static final String QUEUE = "test";
  private static final String EXCHANGE = "test";
  private static final String ERROR_MESSAGE = "Error";

  @Autowired private SendEmailToQueueGateway sendEmailToQueueGateway;

  @MockBean private RabbitTemplate rabbitTemplate;

  @BeforeAll
  static void blockHoundSetup() {
    BlockHound.install();
  }

  @Test
  void blockHoundWorks() {
    try {
      FutureTask<?> task =
          new FutureTask<>(
              () -> {
                Thread.sleep(0); // NOSONAR
                return "";
              });
      Schedulers.parallel().schedule(task);

      task.get(10, TimeUnit.SECONDS);
      fail("should fail");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof BlockingOperationError);
    }
  }

  @Test
  @DisplayName("sendEmail when successfull")
  void sendEmail_WhenSuccessful() {
    willDoNothing()
        .given(rabbitTemplate)
        .convertAndSend(anyString(), anyString(), any(Object.class));

    create(sendEmailToQueueGateway.sendEmail(DEFAULT_EMAIL_ENTITY))
        .expectSubscription()
        .expectNext(SENT)
        .verifyComplete();

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

    create(sendEmailToQueueGateway.sendEmail(DEFAULT_EMAIL_ENTITY))
        .expectSubscription()
        .expectError(RuntimeException.class)
        .verify();

    then(rabbitTemplate)
        .should(times(1))
        .convertAndSend(eq(EXCHANGE), eq(QUEUE), eq(DEFAULT_EMAIL_ENTITY));
  }
}
