/** */
package br.cams7.tests.ms.infra.client;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import br.cams7.tests.ms.infra.client.response.CheckIdentificationNumberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** @author cams7 */
@FeignClient(
    name = "checkIdentificationNumberClient",
    url = "${api.check.identificationNumber.url}")
public interface CheckIdentificationNumberClient {
  @GetMapping(value = "/serasa/{cpf}", produces = APPLICATION_JSON_VALUE)
  CheckIdentificationNumberResponse queryStatus(@PathVariable("cpf") String cpf);
}
