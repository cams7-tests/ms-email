/** */
package br.cams7.tests.ms.infra.client.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** @author cams7 */
@NoArgsConstructor
@Getter
@Setter
public class CheckIdentificationNumberResponse {

  private IdentificationNumberStatusEnum status;

  @JsonCreator
  public CheckIdentificationNumberResponse(
      @JsonProperty("status") IdentificationNumberStatusEnum status) {
    this.status = status;
  }
}
