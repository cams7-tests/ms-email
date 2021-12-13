package br.cams7.tests.ms.infra.configuration;

import br.cams7.tests.ms.EmailApplication;
import br.cams7.tests.ms.core.port.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailUseCase;
import br.cams7.tests.ms.core.port.out.EmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.service.GetAllEmailsUseCaseImpl;
import br.cams7.tests.ms.core.service.GetEmailUseCaseImpl;
import br.cams7.tests.ms.core.service.SendEmailUseCaseImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EmailApplication.class)
public class BeanConfiguration {

  @Bean
  SendEmailUseCase sendEmail(EmailRepository emailRepository, SendEmailService sendEmailService) {
    return new SendEmailUseCaseImpl(emailRepository, sendEmailService);
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
