package br.cams7.tests.ms.infra.configuration;

import static br.cams7.tests.ms.infra.webclient.config.Constants.WEB_CLIENT_CHECK_IDENTIFICATION_NUMBER;

import br.cams7.tests.ms.EmailApplication;
import br.cams7.tests.ms.core.port.in.GetEmailUseCase;
import br.cams7.tests.ms.core.port.in.GetEmailsUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailDirectlyUseCase;
import br.cams7.tests.ms.core.port.in.SendEmailToQueueUseCase;
import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberGateway;
import br.cams7.tests.ms.core.port.out.GetEmailGateway;
import br.cams7.tests.ms.core.port.out.GetEmailsGateway;
import br.cams7.tests.ms.core.port.out.SaveEmailGateway;
import br.cams7.tests.ms.core.port.out.SendEmailGateway;
import br.cams7.tests.ms.core.port.out.SendEmailToQueueGateway;
import br.cams7.tests.ms.core.service.GetEmailUseCaseImpl;
import br.cams7.tests.ms.core.service.GetEmailsUseCaseImpl;
import br.cams7.tests.ms.core.service.SendEmailDirectlyUseCaseImpl;
import br.cams7.tests.ms.core.service.SendEmailToQueueUseCaseImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@ComponentScan(basePackageClasses = EmailApplication.class)
public class BeanConfiguration {

  @Value("${api.check.identificationNumber.url}")
  private String identificationNumberUrl;

  @Bean
  ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Bean
  SendEmailDirectlyUseCase sendEmailDirectly(
      SaveEmailGateway saveEmailGateway,
      SendEmailGateway sendEmailGateway,
      CheckIdentificationNumberGateway checkIdentificationNumberGateway) {
    return new SendEmailDirectlyUseCaseImpl(
        modelMapper(), saveEmailGateway, sendEmailGateway, checkIdentificationNumberGateway);
  }

  @Bean
  SendEmailToQueueUseCase sendEmailToQueueUse(
      SendEmailToQueueGateway sendEmailGateway,
      CheckIdentificationNumberGateway checkIdentificationNumberGateway) {
    return new SendEmailToQueueUseCaseImpl(
        modelMapper(), sendEmailGateway, checkIdentificationNumberGateway);
  }

  @Bean
  GetEmailsUseCase getEmails(GetEmailsGateway getEmailsGateway) {
    return new GetEmailsUseCaseImpl(getEmailsGateway);
  }

  @Bean
  GetEmailUseCase getEmail(GetEmailGateway getEmailGateway) {
    return new GetEmailUseCaseImpl(getEmailGateway);
  }

  @Bean
  @Qualifier(WEB_CLIENT_CHECK_IDENTIFICATION_NUMBER)
  WebClient getCheckIdentificationNumber(WebClient.Builder builder) {
    return builder.baseUrl(identificationNumberUrl).build();
  }
}
