/** */
package br.cams7.tests.ms.core.port.out.exception;

/** @author cams7 */
@SuppressWarnings("serial")
public class SendEmailException extends RuntimeException {

  /**
   * @param message
   * @param cause
   */
  public SendEmailException(String message, Throwable cause) {
    super(message, cause);
  }
}
