/** */
package br.cams7.tests.ms.infra.mq.service;

import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.domain.EmailEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** @author cams7 */
@Component
public class SendEmailToQueueServiceImpl implements SendEmailToQueueService {

  @Value("${rabbitmq.queue}")
  private String queue;

  @Value("${rabbitmq.exchange}")
  private String exchange;

  private final RabbitTemplate rabbitTemplate;

  @Autowired
  public SendEmailToQueueServiceImpl(RabbitTemplate rabbitTemplate) {
    super();
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void sendEmail(EmailEntity email) {
    rabbitTemplate.convertAndSend(exchange, queue, email);
  }
}
