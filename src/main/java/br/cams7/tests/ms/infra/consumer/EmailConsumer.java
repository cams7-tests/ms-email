package br.cams7.tests.ms.infra.consumer;

import br.cams7.tests.ms.core.domain.EmailEntity;
import br.cams7.tests.ms.core.port.in.EmailVO;
import br.cams7.tests.ms.core.port.in.SendEmailUseCase;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {

  private final SendEmailUseCase sendEmailUseCase;

  @Autowired
  EmailConsumer(SendEmailUseCase sendEmailUseCase) {
    super();
    this.sendEmailUseCase = sendEmailUseCase;
  }

  @RabbitListener(queues = "${spring.rabbitmq.queue}")
  void listen(@Payload final EmailDTO dto) {
    EmailEntity email = sendEmailUseCase.sendEmail(getEmail(dto));
    System.out.println("Email Status: " + email.getStatusEmail());
  }

  private static EmailVO getEmail(EmailDTO dto) {
    EmailVO email =
        new EmailVO(
            dto.getOwnerRef(),
            dto.getEmailFrom(),
            dto.getEmailTo(),
            dto.getSubject(),
            dto.getText());
    return email;
  }
}
