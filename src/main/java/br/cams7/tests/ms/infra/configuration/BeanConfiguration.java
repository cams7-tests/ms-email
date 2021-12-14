package br.cams7.tests.ms.infra.configuration;

import br.cams7.tests.ms.EmailApplication;
import br.cams7.tests.ms.core.port.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.core.service.GetAllEmailsUseCaseImpl;
import br.cams7.tests.ms.core.service.GetEmailUseCaseImpl;
import br.cams7.tests.ms.core.service.SendEmailDirectlyUseCaseImpl;
import br.cams7.tests.ms.core.service.SendEmailToQueueUseCaseImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EmailApplication.class)
public class BeanConfiguration {

  @Bean
  SendEmailDirectlyUseCase sendEmailDirectly(
      EmailRepository emailRepository, SendEmailService sendEmailService) {
    return new SendEmailDirectlyUseCaseImpl(emailRepository, sendEmailService);
  }

  @Bean
  SendEmailToQueueUseCase sendEmailToQueueUse(SendEmailToQueueService sendEmailService) {
    return new SendEmailToQueueUseCaseImpl(sendEmailService);
  }

  @Bean
  GetAllEmailsUseCase getAllEmails(EmailRepository emailRepository) {
    return new GetAllEmailsUseCaseImpl(emailRepository);
  }

  @Bean
  GetEmailUseCase getEmail(EmailRepository emailRepository) {
    return new GetEmailUseCaseImpl(emailRepository);
  }

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
