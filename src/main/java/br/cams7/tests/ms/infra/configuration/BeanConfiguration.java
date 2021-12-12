package br.cams7.tests.ms.infra.configuration;

import br.cams7.tests.ms.EmailApplication;
import br.cams7.tests.ms.core.ports.in.GetAllEmailsUseCase;
import br.cams7.tests.ms.core.ports.in.GetEmailUseCase;
import br.cams7.tests.ms.core.ports.in.SendEmailUseCase;
import br.cams7.tests.ms.core.ports.out.EmailRepositoryPort;
import br.cams7.tests.ms.core.ports.out.SendEmailServicePort;
import br.cams7.tests.ms.core.services.GetAllEmailsService;
import br.cams7.tests.ms.core.services.GetEmailService;
import br.cams7.tests.ms.core.services.SendEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EmailApplication.class)
public class BeanConfiguration {

  @Bean
  SendEmailUseCase sendEmail(
      EmailRepositoryPort repository, SendEmailServicePort sendEmailServicePort) {
    return new SendEmailService(repository, sendEmailServicePort);
  }

  @Bean
  GetAllEmailsUseCase getAllEmails(EmailRepositoryPort repository) {
    return new GetAllEmailsService(repository);
  }

  @Bean
  GetEmailUseCase getEmail(EmailRepositoryPort repository) {
    return new GetEmailService(repository);
  }

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
