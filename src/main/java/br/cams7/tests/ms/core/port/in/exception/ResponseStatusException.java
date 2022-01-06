/** */
package br.cams7.tests.ms.core.port.in.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** @author cams7 */
@SuppressWarnings("serial")
@RequiredArgsConstructor
@Getter
public class ResponseStatusException extends RuntimeException {

  private final int status;
}
