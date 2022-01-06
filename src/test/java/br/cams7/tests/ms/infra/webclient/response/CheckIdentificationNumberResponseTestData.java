/** */
package br.cams7.tests.ms.infra.webclient.response;

import static br.cams7.tests.ms.infra.webclient.response.IdentificationNumberStatusEnum.APPROVED;
import static br.cams7.tests.ms.infra.webclient.response.IdentificationNumberStatusEnum.NOT_APPROVED;

public class CheckIdentificationNumberResponseTestData {
  public static CheckIdentificationNumberResponse aprovedCheckIdentificationNumberResponse() {
    var response = new CheckIdentificationNumberResponse(APPROVED);
    return response;
  }

  public static CheckIdentificationNumberResponse notAprovedCheckIdentificationNumberResponse() {
    var response = new CheckIdentificationNumberResponse(NOT_APPROVED);
    return response;
  }
}