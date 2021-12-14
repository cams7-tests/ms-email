/** */
package br.cams7.tests.ms.infra.client;

import static br.cams7.tests.ms.infra.client.response.IdentificationNumberStatusEnum.APPROVED;

import br.cams7.tests.ms.core.port.out.CheckIdentificationNumberService;
import br.cams7.tests.ms.infra.client.response.CheckIdentificationNumberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author cams7 */
@Component
public class CheckIdentificationNumberServiceImpl implements CheckIdentificationNumberService {

  private final CheckIdentificationNumberClient client;

  @Autowired
  public CheckIdentificationNumberServiceImpl(CheckIdentificationNumberClient client) {
    super();
    this.client = client;
  }

  @Override
  public boolean isValid(String identificationNumber) {
    CheckIdentificationNumberResponse response = client.queryStatus(identificationNumber);

    return APPROVED.equals(response.getStatus());
  }
}
