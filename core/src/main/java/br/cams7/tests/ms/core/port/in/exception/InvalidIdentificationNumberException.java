/** */
package br.cams7.tests.ms.core.port.in.exception;

public class InvalidIdentificationNumberException extends RuntimeException {

  private static final long serialVersionUID = 462084279044244141L;

  public InvalidIdentificationNumberException(String identificationNumber) {
    super(String.format("The identification number \"%s\" isn't valid", identificationNumber));
  }
}
