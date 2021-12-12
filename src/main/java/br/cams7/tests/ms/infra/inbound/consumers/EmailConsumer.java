package br.cams7.tests.ms.infra.inbound.consumers;

import br.cams7.tests.ms.core.domain.Email;
import br.cams7.tests.ms.core.ports.EmailServicePort;
import br.cams7.tests.ms.infra.dtos.EmailDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

  @Autowired EmailServicePort emailServicePort;

  @RabbitListener(queues = "${spring.rabbitmq.queue}")
  public void listen(@Payload EmailDto emailDto) {
    Email email = new Email();
    BeanUtils.copyProperties(emailDto, email);
    emailServicePort.sendEmail(email);
    System.out.println("Email Status: " + email.getStatusEmail().toString());
  }
}
