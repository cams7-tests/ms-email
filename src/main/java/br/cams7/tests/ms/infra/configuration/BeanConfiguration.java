package br.cams7.tests.ms.infra.configuration;

import br.cams7.tests.ms.EmailApplication;
import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.core.port.out.GetEmailRepository;
import br.cams7.tests.ms.core.port.out.GetEmailsRepository;
import br.cams7.tests.ms.core.port.out.SaveEmailRepository;
import br.cams7.tests.ms.core.port.out.SendEmailService;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueService;
import br.cams7.tests.ms.core.service.GetEmailUseCaseImpl;
import br.cams7.tests.ms.core.service.GetEmailsUseCaseImpl;
import br.cams7.tests.ms.core.service.SendEmailDirectlyUseCaseImpl;
import br.cams7.tests.ms.core.service.SendEmailToQueueUseCaseImpl;
import org.modelmapper.ModelMapper;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = EmailApplication.class)
@EnableFeignClients(basePackages = "br.cams7.tests.ms.infra.client")
public class BeanConfiguration {

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  SendEmailDirectlyUseCase sendEmailDirectly(
      SaveEmailRepository saveEmailRepository,
      SendEmailService sendEmailService,
      CheckIdentificationNumberService checkIdentificationNumberService) {
    return new SendEmailDirectlyUseCaseImpl(
        modelMapper(), saveEmailRepository, sendEmailService, checkIdentificationNumberService);
  }

  @Bean
  SendEmailToQueueUseCase sendEmailToQueueUse(
      SendEmailToQueueService sendEmailService,
      CheckIdentificationNumberService checkIdentificationNumberService) {
    return new SendEmailToQueueUseCaseImpl(
        modelMapper(), sendEmailService, checkIdentificationNumberService);
  }

  @Bean
  GetEmailsUseCase getEmails(GetEmailsRepository getEmailsRepository) {
    return new GetEmailsUseCaseImpl(modelMapper(), getEmailsRepository);
  }

  @Bean
  GetEmailUseCase getEmail(GetEmailRepository getEmailRepository) {
    return new GetEmailUseCaseImpl(modelMapper(), getEmailRepository);
  }
}
