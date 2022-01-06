/** */
package br.cams7.tests.ms.core.port.out;

import reactor.core.publisher.Mono;

/** @author cams7 */
public interface CheckIdentificationNumberService {

  Mono<Boolean> isValid(String identificationNumber);
}
