/** */
package br.cams7.tests.ms.infra.client;

import static br.cams7.tests.ms.infra.client.response.IdentificationNumberStatusEnum.APPROVED;
import static br.cams7.tests.ms.infra.configuration.BeanConfiguration.WEB_CLIENT_CHECK_IDENTIFICATION_NUMBER;

import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.infra.client.response.CheckIdentificationNumberResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CheckIdentificationNumberServiceImpl implements CheckIdentificationNumberService {

  private final WebClient checkIdentificationNumber;

  CheckIdentificationNumberServiceImpl(
      @Qualifier(WEB_CLIENT_CHECK_IDENTIFICATION_NUMBER) WebClient checkIdentificationNumber) {
    super();
    this.checkIdentificationNumber = checkIdentificationNumber;
  }

  @Override
  public Mono<Boolean> isValid(String identificationNumber) {
    return checkIdentificationNumber
        .get()
        .uri("/serasa/{cpf}", identificationNumber)
        .retrieve()
        .bodyToMono(CheckIdentificationNumberResponse.class)
        .map(response -> APPROVED.equals(response.getStatus()));
  }
}
