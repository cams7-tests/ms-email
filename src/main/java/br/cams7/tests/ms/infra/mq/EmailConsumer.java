package br.cams7.tests.ms.infra.mq;

import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import javax.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

  private final SendEmailDirectlyUseCase sendEmailUseCase;

  @RabbitListener(queues = "${rabbitmq.queue}")
  Mono<Void> listen(@Payload final EmailDTO email) {
    try {
      return sendEmailUseCase.execute(getEmail(email)).flatMap(savedEmail -> Mono.empty());
    } catch (ConstraintViolationException e) {
      throw new AmqpRejectAndDontRequeueException(e.getMessage(), e.getCause());
    }
  }

  private static EmailVO getEmail(EmailDTO dto) {
    return new EmailVO(
        dto.getOwnerRef(), dto.getEmailFrom(), dto.getEmailTo(), dto.getSubject(), dto.getText());
  }
}
