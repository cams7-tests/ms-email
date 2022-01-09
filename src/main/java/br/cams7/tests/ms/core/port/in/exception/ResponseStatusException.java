/** */
package br.cams7.tests.ms.core.port.in.exception;

import lombok.Getter;

@Getter
public class ResponseStatusException extends RuntimeException {

  private static final long serialVersionUID = -5617914239548104967L;

  private final int status;

  public ResponseStatusException(String message, int status) {
    super(message);
    this.status = status;
  }
}
