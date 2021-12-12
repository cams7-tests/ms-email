package br.cams7.tests.ms.infra.configuration;

import br.cams7.tests.ms.EmailApplication;
import br.cams7.tests.ms.core.ports.EmailRepositoryPort;
import br.cams7.tests.ms.core.ports.SendEmailServicePort;
import br.cams7.tests.ms.core.services.EmailServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EmailApplication.class)
public class BeanConfiguration {

  @Bean
  EmailServiceImpl emailService(
      EmailRepositoryPort repository, SendEmailServicePort sendEmailServicePort) {
    return new EmailServiceImpl(repository, sendEmailServicePort);
  }

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }
}
