/** */
package br.cams7.tests.ms.infra.mq.service;

import br.cams7.tests.ms.core.port.out.SendEmailToQueueGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.infra.logging.LogEntryExit;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** @author cams7 */
@Component
@RequiredArgsConstructor
public class SendEmailToQueueService implements SendEmailToQueueGateway {

  @Value("${rabbitmq.queue}")
  private String queue;

  @Value("${rabbitmq.exchange}")
  private String exchange;

  private final RabbitTemplate rabbitTemplate;

  @LogEntryExit(showArgs = true)
  @Override
  public void sendEmail(EmailEntity email) {
    rabbitTemplate.convertAndSend(exchange, queue, email);
  }
}
