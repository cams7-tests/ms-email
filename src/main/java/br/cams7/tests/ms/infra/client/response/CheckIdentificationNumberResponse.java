/** */
package br.cams7.tests.ms.infra.client.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckIdentificationNumberResponse {

  private final IdentificationNumberStatusEnum status;

  @JsonCreator
  public CheckIdentificationNumberResponse(
      @JsonProperty("status") IdentificationNumberStatusEnum status) {
    this.status = status;
  }
}
