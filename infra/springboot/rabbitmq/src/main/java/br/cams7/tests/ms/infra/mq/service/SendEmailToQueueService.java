/** */
package br.cams7.tests.ms.infra.mq.service;

import static br.cams7.tests.ms.domain.EmailStatusEnum.SENT;

import br.cams7.tests.ms.core.port.out.SendEmailToQueueGateway;
import br.cams7.tests.ms.domain.EmailEntity;
import br.cams7.tests.ms.domain.EmailStatusEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SendEmailToQueueService implements SendEmailToQueueGateway {

  @Value("${rabbitmq.queue}")
  private String queue;

  @Value("${rabbitmq.exchange}")
  private String exchange;

  private final RabbitTemplate rabbitTemplate;

  @Override
  public Mono<EmailStatusEnum> sendEmail(EmailEntity email) {
    return Mono.fromCallable(
        () -> {
          rabbitTemplate.convertAndSend(exchange, queue, email);
          return SENT;
        });
  }
}
