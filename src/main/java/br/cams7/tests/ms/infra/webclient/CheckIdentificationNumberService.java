/** */
package br.cams7.tests.ms.infra.webclient;

import static br.cams7.tests.ms.infra.configuration.BeanConfiguration.WEB_CLIENT_CHECK_IDENTIFICATION_NUMBER;
import static br.cams7.tests.ms.infra.webclient.response.IdentificationNumberStatusEnum.APPROVED;

import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberGateway;
import br.cams7.tests.ms.infra.logging.LogEntryExit;
import br.cams7.tests.ms.infra.webclient.response.CheckIdentificationNumberResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CheckIdentificationNumberService implements CheckIdentificationNumberGateway {

  private final WebClient checkIdentificationNumberWebClient;

  CheckIdentificationNumberService(
      @Qualifier(WEB_CLIENT_CHECK_IDENTIFICATION_NUMBER)
          WebClient checkIdentificationNumberWebClient) {
    super();
    this.checkIdentificationNumberWebClient = checkIdentificationNumberWebClient;
  }

  @LogEntryExit(showArgs = true, showResult = true)
  @Override
  public Mono<Boolean> isValid(String identificationNumber) {
    return checkIdentificationNumberWebClient
        .get()
        .uri("/serasa/{cpf}", identificationNumber)
        .retrieve()
        .bodyToMono(CheckIdentificationNumberResponse.class)
        .map(response -> APPROVED.equals(response.getStatus()));
  }
}
