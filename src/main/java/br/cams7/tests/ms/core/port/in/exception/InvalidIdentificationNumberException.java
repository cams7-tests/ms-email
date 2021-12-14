/** */
package br.cams7.tests.ms.core.port.in.exception;

/** @author cams7 */
@SuppressWarnings("serial")
public class InvalidIdentificationNumberException extends RuntimeException {

  /** @param message */
  public InvalidIdentificationNumberException(String identificationNumber) {
    super(String.format("The identification number \"%s\" isn't valid", identificationNumber));
  }
}
