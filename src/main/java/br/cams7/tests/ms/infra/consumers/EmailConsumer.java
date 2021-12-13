package br.cams7.tests.ms.infra.consumers;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.ports.in.SendEmailUseCase;
import br.cams7.tests.ms.infra.dtos.EmailDto;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

  private final SendEmailUseCase sendEmailUseCase;
  private final ModelMapper modelMapper;

  @Autowired
  EmailConsumer(SendEmailUseCase sendEmailUseCase, ModelMapper modelMapper) {
    super();
    this.sendEmailUseCase = sendEmailUseCase;
    this.modelMapper = modelMapper;
  }

  @RabbitListener(queues = "${spring.rabbitmq.queue}")
  void listen(@Payload final EmailDto emailDto) {
    EmailEntity email = getEmail(emailDto);
    sendEmailUseCase.sendEmail(email);
    System.out.println("Email Status: " + email.getStatusEmail().toString());
  }

  private EmailEntity getEmail(EmailDto emailDto) {
    return modelMapper.map(emailDto, EmailEntity.class);
  }
}
